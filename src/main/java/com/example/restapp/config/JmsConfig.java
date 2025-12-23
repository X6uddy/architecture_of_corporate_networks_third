package com.example.restapp.config;

import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class JmsConfig
{
    @Value("${app.messaging.audit-destination}")
    private String auditDestinationName;

    @Value("${app.messaging.notification-destination}")
    private String notificationDestinationName;

    @Bean
    public Queue auditDestination()
    {
        return new ActiveMQQueue(auditDestinationName);
    }

    @Bean
    public Queue notificationDestination()
    {
        return new ActiveMQQueue(notificationDestinationName);
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter()
    {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}


