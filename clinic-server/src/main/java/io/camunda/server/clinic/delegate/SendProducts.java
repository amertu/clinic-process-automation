package io.camunda.server.clinic.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SendProducts implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(SendProducts.class);
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Sending products to the customer...");
        // Simulate sending products logic here,
        // For example, you might call an external service or perform some business logic
        String orderId = (String) execution.getVariable("orderId");
        if (orderId == null) {
            log.error("Order ID is missing in the execution context.");
            throw new IllegalStateException("Order ID is required to send products.");
        }
        log.info("Products sent for order ID: {}", orderId);


    }
}
