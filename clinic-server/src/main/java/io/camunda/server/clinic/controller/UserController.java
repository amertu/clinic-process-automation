package io.camunda.server.clinic.controller;

import ch.qos.logback.classic.Logger;
import io.camunda.server.clinic.dto.*;
import io.camunda.server.clinic.repository.TreatmentRepository;
import io.camunda.server.clinic.repository.UserRepository;
import io.camunda.server.clinic.service.ProcessService;
import io.camunda.server.clinic.validation.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class UserController {

    Logger LOG = (Logger) LoggerFactory.getLogger(UserController.class);
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity <User> registerUser(@RequestBody User user) {
        LOG.info("Received registration for user: {}", user.getUserName());
        boolean existingUser = userRepository.findByEmail(user.getEmail()).isPresent();
        if (existingUser) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/authenticate")
    public boolean authenticateUser(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + user.getEmail() + " not found"));
        LOG.info("Authentication user with name {} done.", foundUser.getUserName());
        return user.getPassword().equals(foundUser.getPassword());
    }

    @PostMapping("/start")
    public ResponseEntity <User> startProcessManually(@RequestBody User user) {
        processService.startUserProcess(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> loginUser(@RequestBody User user) {

        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getPassword().equals(foundUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Invalid password", null));
        }

        ProcessInstance instance = processService.getUserProcessInstance(foundUser);

        return ResponseEntity.ok(new ApiResponse<User>("login succeed", instance.getProcessInstanceId(), foundUser));
    }

    @PostMapping("/request-treatment")
    public ResponseEntity<String> sendTreatmentRequest(@RequestBody Treatment treatment) {
        treatmentRepository.save(treatment);
        processService.startClinicProcess(treatment);
        return ResponseEntity.ok("Treatment request sent and process started.");
    }

    @GetMapping("/task")
    public ResponseEntity<ApiResponse<ProcessTask>> getTask(@RequestBody String processId) {
        Task task = processService.getTaskByProcessInstanceId(processId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Task not found", null));
        }

        ProcessTask dto = TaskMapper.from(task);
        return ResponseEntity.ok(new ApiResponse<ProcessTask>("Task found", task.getProcessInstanceId(), dto));
    }


}