package io.camunda.server.clinic.delegate;

import ch.qos.logback.classic.Logger;
import io.camunda.server.clinic.dto.User;
import io.camunda.server.clinic.repository.UserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EligibilityCheck implements JavaDelegate {
    Logger LOG = (Logger) LoggerFactory.getLogger(EligibilityCheck.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) {
        Long userId = (Long) execution.getVariable("userId");
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LOG.info("Eligibility check for user: {}", foundUser.getEmail());
    }
}
