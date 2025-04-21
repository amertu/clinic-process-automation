package io.camunda.server.clinic.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class GatewayDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("registered",false);
        execution.setVariable("ok",true);
        execution.setVariable("declined",false);
        execution.setVariable("cancelled",false);
        execution.setVariable("routine",false);
        execution.setVariable("special",false);
        execution.setVariable("error",false);
    }
}
