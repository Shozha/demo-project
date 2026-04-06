package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.mapper.TransactionMapper;
import ru.itis.dto.request.TransactionRequest;
import ru.itis.dto.response.TransactionElementResponse;
import ru.itis.dto.response.TransactionResponse;
import ru.itis.model.Contract;
import ru.itis.model.Transaction;
import ru.itis.service.ContractService;
import ru.itis.service.TransactionService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransactionController {

    private final TransactionService transactionService;
    private final ContractService contractService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/transactions/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable("name") String name) {
        log.info("Received request to get transactions for contract name: {}", name);
        Contract contract = contractService.getByContractName(name);

        List<Transaction> transactions = transactionService.findAllTransactionsByContractName(name);
        Instant now = Instant.now();
        log.debug("Returning transaction response with {} transactions", transactions.size());
        return ResponseEntity.ok(
                transactionMapper.toResponse(
                        contract.getContractName(),
                        now,
                        now,
                        transactions
                )
        );
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionElementResponse> createTransaction(@RequestBody TransactionRequest request) {
        log.info("Received request to create transaction: {}", request);
        UUID sourceId = contractService.getByContractName(request.getSourceContractName()).getId();
        UUID targetId = contractService.getByContractName(request.getTargetContractName()).getId();
        Transaction transaction = transactionService.save(
                sourceId,
                targetId,
                request.getAmount(),
                request.getDescription()
        );
        log.info("Transaction created successfully with id: {}", transaction.getId());
        return ResponseEntity.ok(transactionMapper.toElementResponse(transaction));
    }
}
