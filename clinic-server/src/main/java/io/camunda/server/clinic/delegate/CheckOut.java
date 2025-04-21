package io.camunda.server.clinic.delegate;

import ch.qos.logback.classic.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.LoggerFactory;

public class CheckOut implements JavaDelegate {
    Logger LOG = (Logger) LoggerFactory.getLogger(CheckOut.class);
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String customerName = (String) execution.getVariable("user_name");
        double totalAmount = 30.0; // This would typically be calculated based on the services provided
        LOG.info("Processing checkout for customer: {}", customerName);
        LOG.info("Total amount: {}$", totalAmount);

        // Add a new variable or update an existing variable in the process
        double discount = 0.1; // 10% discount
        double discountedAmount = totalAmount - (totalAmount * discount);
        execution.setVariable("discountedAmount", discountedAmount);

    }
}

