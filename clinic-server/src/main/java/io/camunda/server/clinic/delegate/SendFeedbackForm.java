package io.camunda.server.clinic.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendFeedbackForm")
public class SendFeedbackForm implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // Dein Business-Logic hier
        System.out.println("Feedback-Formular wurde gesendet.");
    }
}
