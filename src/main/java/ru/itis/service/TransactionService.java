package ru.itis.service;

import ru.itis.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction save(UUID sourceContractId, UUID targetContractId, BigDecimal amount, String description);

    Transaction findById(UUID transactionId);

    List<Transaction> findAllTransactionsByContractId(UUID sourceContractId);

    List<Transaction> findAllTransactionsByContractName(String contractName);

    List<Transaction> findAllTransactions();

}
