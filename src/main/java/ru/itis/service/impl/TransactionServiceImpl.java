package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.client.CardsClient;
import ru.itis.client.UserRestrictionsClient;
import ru.itis.dto.response.client.CardResponse;
import ru.itis.dto.response.client.UserRestrictionResponse;
import ru.itis.exception.InvalidAmountException;
import ru.itis.exception.TransactionNotFoundException;
import ru.itis.exception.UserBlockedException;
import ru.itis.model.Contract;
import ru.itis.model.Transaction;
import ru.itis.repository.TransactionRepository;
import ru.itis.service.ContractService;
import ru.itis.service.TransactionService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ContractService contractService;
    private final UserRestrictionsClient userRestrictionsClient;
    private final CardsClient cardsClient;

    @Override
    @Transactional
    public Transaction save(UUID sourceContractId, UUID targetContractId, BigDecimal amount, String description) {
        log.info("Initiating transfer: amount={} from contract={} to contract={}", amount, sourceContractId, targetContractId);
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException();
        }

        Contract source = contractService.getById(sourceContractId);
        Contract target = contractService.getById(targetContractId);

        log.debug("Fetching card info from CardsClient for contracts");
        CardResponse sourceCard = cardsClient.getCardByContractName(source.getContractName());
        log.debug("Source card info: userId={}", sourceCard.getUserId());

        CardResponse targetCard = cardsClient.getCardByContractName(target.getContractName());
        log.debug("Target card info: userId={}", targetCard.getUserId());

        UUID sourceUserId = UUID.fromString(sourceCard.getUserId());
        UUID targetUserId = UUID.fromString(targetCard.getUserId());

        log.debug("Checking user restrictions from UserRestrictionsClient");
        UserRestrictionResponse src = userRestrictionsClient.getRestrictions(sourceUserId);
        log.debug("User {} restrictions: blocked={}", sourceUserId, src.isBlocked());
        if (src.isBlocked()) {
            throw new UserBlockedException(sourceUserId);
        }

        UserRestrictionResponse trg = userRestrictionsClient.getRestrictions(targetUserId);
        log.debug("User {} restrictions: blocked={}", targetUserId, trg.isBlocked());
        if (trg.isBlocked()) {
            throw new UserBlockedException(targetUserId);
        }

        contractService.processTransfer(source, target, amount);

        return transactionRepository.save(
                Transaction.builder()
                        .sourceContractId(sourceContractId)
                        .targetContractId(targetContractId)
                        .sourceUserId(sourceUserId)
                        .targetUserId(targetUserId)
                        .amount(amount)
                        .description(description)
                        .createdAt(Instant.now())
                        .build());
    }

    @Override
    public Transaction findById(UUID transactionId) {
        log.debug("Fetching transaction by id: {}", transactionId);
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public List<Transaction> findAllTransactionsByContractId(UUID sourceContractId) {
        log.debug("Fetching all transactions for contract id: {}", sourceContractId);
        return transactionRepository.findAllBySourceContractId(sourceContractId);
    }

    @Override
    public List<Transaction> findAllTransactionsByContractName(String contractName) {
        log.debug("Fetching all transactions for contract name: {}", contractName);
        return transactionRepository.findAllByContractName(contractName);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        log.debug("Fetching all transactions");
        return transactionRepository.findAll();
    }
}
