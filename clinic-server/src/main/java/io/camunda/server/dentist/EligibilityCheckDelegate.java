package io.camunda.server.dentist;

import ch.qos.logback.classic.Logger;
import io.camunda.server.dentist.dto.User;
import io.camunda.server.dentist.repository.UserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EligibilityCheckDelegate implements JavaDelegate {
    Logger LOG = (Logger) LoggerFactory.getLogger(EligibilityCheckDelegate.class);
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
