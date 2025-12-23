package com.example.restapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "audit_log", schema = "work")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, length = 20)
    private ChangeType changeType;

    @Column(name = "entity_name", nullable = false, length = 255)
    private String entityName;

    @Column(name = "entity_id", nullable = false, length = 255)
    private String entityId;

    @Lob
    @Column(name = "payload")
    private String payload;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}


