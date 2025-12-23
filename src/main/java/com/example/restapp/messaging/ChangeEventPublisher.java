package com.example.restapp.messaging;

import com.example.restapp.model.ChangeType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class ChangeEventPublisher
{
    private final JmsTemplate jmsTemplate;
    private final Queue auditDestination;
    private final Queue notificationDestination;
    private final ObjectMapper objectMapper;

    public ChangeEventPublisher(JmsTemplate jmsTemplate,
                                @Qualifier("auditDestination") Queue auditDestination,
                                @Qualifier("notificationDestination") Queue notificationDestination,
                                ObjectMapper objectMapper)
    {
        this.jmsTemplate = jmsTemplate;
        this.auditDestination = auditDestination;
        this.notificationDestination = notificationDestination;
        this.objectMapper = objectMapper;
    }

    public void publish(ChangeType changeType, String entityName, String entityId, Object payloadSource)
    {
        Map<String, Object> payload = objectMapper.convertValue(payloadSource, new TypeReference<>() {});
        ChangeEvent event = ChangeEvent.builder()
                .changeType(changeType)
                .entityName(entityName)
                .entityId(entityId)
                .payload(payload)
                .occurredAt(Instant.now())
                .build();

        jmsTemplate.convertAndSend(auditDestination, event);
        jmsTemplate.convertAndSend(notificationDestination, event);
    }
}


