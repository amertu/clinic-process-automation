package io.camunda.server.dentist.dto;

public class ApiResponse<T> {
    private String message;
    private String processInstanceId;
    private T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String message, String processInstanceId, T data) {
        this.message = message;
        this.processInstanceId = processInstanceId;
        this.data = data;
    }

    public ApiResponse() {
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

}
