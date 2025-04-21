package io.camunda.server.clinic.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderProducts implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderProducts.class);
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        LOGGER.info("Ordering Process started....");

        LOGGER.info(execution.getActivityInstanceId());
        execution.setVariable("cancelled",true);

    }
}
