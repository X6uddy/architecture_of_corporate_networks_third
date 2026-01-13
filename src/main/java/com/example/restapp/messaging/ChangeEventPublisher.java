package com.example.restapp.messaging;

import com.example.restapp.model.ChangeType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Topic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class ChangeEventPublisher
{
    private final JmsTemplate jmsTemplate;
    private final Topic changeEventsTopic;
    private final ObjectMapper objectMapper;

    public ChangeEventPublisher(JmsTemplate jmsTemplate,
                                @Qualifier("changeEventsTopic") Topic changeEventsTopic,
                                ObjectMapper objectMapper)
    {
        this.jmsTemplate = jmsTemplate;
        this.changeEventsTopic = changeEventsTopic;
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

        jmsTemplate.convertAndSend(changeEventsTopic, event);
    }
}


