package com.technokratos.agona.repository;

import com.technokratos.agona.entity.DeletedFileLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DeletedFileLogRepository extends JpaRepository<DeletedFileLog, UUID> {

    @Query("SELECT d FROM DeletedFileLog d WHERE d.createdAt < :cutoffTime")
    List<DeletedFileLog> findOldLogs(Instant cutoffTime, Pageable pageable);
}
