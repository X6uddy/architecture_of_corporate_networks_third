package com.example.restapp.repository;

import com.example.restapp.model.AuditLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogEntry, Long>
{
}


