package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllBySourceContractId(UUID sourceContractId);

    @Query(
            value = """
            SELECT t.*
            FROM transactions t
            JOIN contracts c
              ON t.source_contract_id = c.id
              OR t.target_contract_id = c.id
            WHERE c.contract_name = ?
            """,
            nativeQuery = true
    )
    List<Transaction> findAllByContractName(String contractName);
}
