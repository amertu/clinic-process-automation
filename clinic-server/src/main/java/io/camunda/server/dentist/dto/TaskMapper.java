package io.camunda.server.dentist.dto;

import org.camunda.bpm.engine.task.Task;

public class TaskMapper {
    public static ProcessTaskDto from(Task task) {
        return new ProcessTaskDto(
                task.getId(),
                task.getName(),
                task.getAssignee(),
                task.getProcessInstanceId(),
                task.getCreateTime() != null ? task.getCreateTime().toString() : null,
                task.getDueDate() != null ? task.getDueDate().toString() : null
        );
    }
}
