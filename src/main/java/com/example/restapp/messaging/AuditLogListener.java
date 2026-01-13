package com.example.restapp.messaging;

import com.example.restapp.model.AuditLogEntry;
import com.example.restapp.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener
{
    private static final Logger log = LoggerFactory.getLogger(AuditLogListener.class);

    private final AuditLogRepository repository;
    private final ObjectMapper objectMapper;

    public AuditLogListener(AuditLogRepository repository, ObjectMapper objectMapper)
    {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "${app.messaging.change-events-topic}", containerFactory = "jmsListenerContainerFactory")
    public void onChangeEvent(ChangeEvent event) throws Exception
    {
        if (event == null || event.getChangeType() == null || event.getEntityName() == null || event.getEntityId() == null)
        {
            log.warn("Received invalid change event: {}", event);
            return;
        }

        AuditLogEntry entry = new AuditLogEntry();
        entry.setChangeType(event.getChangeType());
        entry.setEntityName(event.getEntityName());
        entry.setEntityId(event.getEntityId());
        entry.setOccurredAt(event.getOccurredAt());
        entry.setPayload(objectMapper.writeValueAsString(event.getPayload()));
        repository.save(entry);
    }
}


