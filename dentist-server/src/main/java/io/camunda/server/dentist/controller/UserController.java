package io.camunda.server.dentist.controller;

import ch.qos.logback.classic.Logger;
import io.camunda.server.dentist.model.User;
import io.camunda.server.dentist.repository.UserRepository;
import io.camunda.server.dentist.service.ProcessService;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class UserController {

    Logger LOG = (Logger) LoggerFactory.getLogger(UserController.class);
    private final ProcessService processService;
    private final UserRepository userRepository;

    public UserController(ProcessService processService, UserRepository userRepository) {
        this.processService = processService;
        this.userRepository = userRepository;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity <User> registerUser(@RequestBody User user) {
        LOG.info("Received registration for user: {}", user.getName());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/authenticate")
    public boolean authenticateUser(@RequestBody boolean dummy) {
        LOG.info("🔐 Dummy authentication done.");
        return true;
    }

    @PostMapping("/start")
    public ResponseEntity <User> startProcessManually(@RequestBody User user) {
        processService.startUserProcess(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public boolean startLogin() {
        LOG.info("🔐 Dummy login start");
        return true;
    }

    @PostMapping("/client/login?name={name}")
    public ResponseEntity <User> loginUser(User user) {
        LOG.info("login task start");
        User savedUser = userRepository.findByUsernameAndPassword(user.getName(), user.getPassword())
                .orElse(null);
        if (savedUser == null) {
            LOG.error("User not found with ID: {}", user.getId());
            return ResponseEntity.notFound().build();
        }
        LOG.info("User found: {}", savedUser.getName());
        processService.completeLoginTask(savedUser);
        return ResponseEntity.ok(savedUser);
    }
}