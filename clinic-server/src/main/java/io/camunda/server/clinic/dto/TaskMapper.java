package io.camunda.server.clinic.dto;

import org.camunda.bpm.engine.task.Task;

public class TaskMapper {
    public static ProcessTask from(Task task) {
        return new ProcessTask(
                task.getId(),
                task.getName(),
                task.getAssignee(),
                task.getProcessInstanceId(),
                task.getCreateTime() != null ? task.getCreateTime().toString() : null,
                task.getDueDate() != null ? task.getDueDate().toString() : null
        );
    }
}
