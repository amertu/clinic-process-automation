package io.camunda.server.clinic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class LabOrderListener {
    private static final Logger log = LoggerFactory.getLogger(LabOrderListener.class);
    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public LabOrderListener(RuntimeService runtimeService, ObjectMapper objectMapper) {
        this.runtimeService = runtimeService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-topic", groupId = "lab")
    public void receive(String payload,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {

        log.debug("offset={} partition={} payload={}", offset, partition, payload);
        JsonNode node;
        try {
            node = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            log.error("Bad JSON: {}", payload, e);
            return;
        }

        JsonNode idNode = node.get("orderId");
        if (idNode == null || idNode.isNull()) {
            log.error("orderId missing in {}", payload);
            return;
        }
        String orderId = idNode.asText();
        try {
            runtimeService.createMessageCorrelation("LabProcessStartMessage")
                    .setVariable("orderId", orderId)
                    .correlateStartMessage();
            log.info("Started lab process for orderId={}", orderId);
        } catch (Exception e) {
            log.error("Failed to correlate message", e);
        }

    }


}
