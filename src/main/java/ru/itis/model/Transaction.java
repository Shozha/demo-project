package ru.itis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "source_contract_id", nullable = false, columnDefinition = "uuid")
    private UUID sourceContractId;

    @Column(name = "target_contract_id", nullable = false, columnDefinition = "uuid")
    private UUID targetContractId;

    @Column(name = "source_user_id", nullable = false, columnDefinition = "uuid")
    private UUID sourceUserId;

    @Column(name = "target_user_id", nullable = false, columnDefinition = "uuid")
    private UUID targetUserId;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
