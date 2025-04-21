package io.camunda.server.clinic.dto;

public class ProcessTask {
    private String taskId;
    private String name;
    private String assignee;
    private String processInstanceId;
    private String created;
    private String due;

    public ProcessTask() {}

    public ProcessTask(String taskId, String name, String assignee, String processInstanceId, String created, String due) {
        this.taskId = taskId;
        this.name = name;
        this.assignee = assignee;
        this.processInstanceId = processInstanceId;
        this.created = created;
        this.due = due;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }
}
