package com.example.restapp.messaging;

import com.example.restapp.model.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEvent
{
    private ChangeType changeType;
    private String entityName;
    private String entityId;
    private Map<String, Object> payload;
    private Instant occurredAt;
}


