package com.bootcamp.multipolar.gatewaybank.service;

import com.bootcamp.multipolar.gatewaybank.kafka.AccessLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AccessLogService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logAccess(AccessLog accessLog){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String accessLogMessage = objectMapper.writeValueAsString(accessLog);
            kafkaTemplate.send("access_logs_bank", accessLogMessage);
        }catch (KafkaException | JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
