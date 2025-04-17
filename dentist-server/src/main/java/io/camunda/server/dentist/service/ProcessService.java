package io.camunda.server.dentist.service;

import io.camunda.server.dentist.model.User;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessService {

    private final RuntimeService runtimeService;
    private TaskService taskService;

    public ProcessService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void startUserProcess(User user) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("user_name", user.getName());
        vars.put("password", user.getPassword());
        vars.put("number", user.getPhoneNumber());

        runtimeService.startProcessInstanceByKey("user_process", vars);
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
        vars.put("user_name", user.getName());
        vars.put("password", user.getPassword());
        vars.put("number", user.getPhoneNumber());

        taskService.complete(task.getId(), vars);
    }
}