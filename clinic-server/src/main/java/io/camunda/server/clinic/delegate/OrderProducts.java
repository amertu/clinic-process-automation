package io.camunda.server.clinic.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("orderProducts")
public class OrderProducts implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducts.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OrderProducts(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public record OrderEvent(String orderId, String productId) {}

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        LOGGER.info("Ordering Process started....");
        execution.setVariable("cancelled", true);

        String orderId = UUID.randomUUID().toString();

        OrderEvent event = new OrderEvent(orderId, "123");
        String payload = new ObjectMapper().writeValueAsString(event);

        kafkaTemplate.send("order-topic", orderId, payload);
        execution.setVariable("orderId", orderId);
    }
}
