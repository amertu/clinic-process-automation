package io.camunda.server.dentist.service;

import ch.qos.logback.classic.Logger;
import io.camunda.server.dentist.dto.Treatment;
import io.camunda.server.dentist.dto.User;
import io.camunda.server.dentist.repository.TreatmentRepository;
import io.camunda.server.dentist.repository.UserRepository;
import io.camunda.server.dentist.validation.ResourceNotFoundException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessService {
    Logger LOG = (Logger) LoggerFactory.getLogger(ProcessService.class);
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;

    public ProcessInstance startUserProcess(User user) {
        LOG.info("Starting user process for user: {}", user.getEmail());
        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + user.getEmail() + " not found"));
        return runtimeService.startProcessInstanceByKey("user_process", Map.of("userId", foundUser.getId()));
    }

    public void startClinicProcess(Treatment treatment) {
        runtimeService.startProcessInstanceByKey("dental_clinic_process", Map.of("treatmentId", treatment.getId()));
    }

    public void completeLoginTask(User user) {
        Task task = taskService.createTaskQuery()
                .taskDefinitionKey("login_user")
                .processVariableValueEquals("userId", user.getId())
                .singleResult();

        if (task == null) {
            startUserProcess(user);
            task = taskService.createTaskQuery()
                    .taskDefinitionKey("login_user")
                    .processVariableValueEquals("userId", user.getId())
                    .singleResult();
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put("user_name", user.getUsername());
        vars.put("password", user.getPassword());
        vars.put("number", user.getPhoneNumber());

        taskService.complete(task.getId(), vars);
    }

    public ProcessInstance getUserProcessInstance(User user) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("user_process")
                .variableValueEquals("userId", user.getId())
                .singleResult();

        if (instance != null) {
            return instance;
        }
        return startUserProcess(user);
    }

    public ProcessInstance startNewUserProcess(User user) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "dental_appointment_process",
                Map.of("userId", user.getId(), "email", user.getEmail())
        );

        Treatment treatment = new Treatment();
        treatment.setUserId(user.getId());
        treatment.setProcessInstanceId(processInstance.getId());
        treatment.setStatus(Treatment.AppointmentStatus.ACTIVE);
        treatmentRepository.save(treatment);

        return processInstance;
    }

    public void cancelAppointment(Long appointmentId) {
        Treatment treatment = treatmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        treatment.setStatus(Treatment.AppointmentStatus.CANCELED);
        treatmentRepository.save(treatment);

        runtimeService.deleteProcessInstance(treatment.getProcessInstanceId(), "Appointment canceled");
    }

    public void completeAppointment(Long appointmentId) {
        Treatment treatment = treatmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        treatment.setStatus(Treatment.AppointmentStatus.COMPLETED);
        treatmentRepository.save(treatment);

        runtimeService.deleteProcessInstance(treatment.getProcessInstanceId(), "Appointment completed");
    }

    public Task getTaskByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .singleResult();
    }


}