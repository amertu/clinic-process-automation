package io.camunda.client.clinic.dto;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Treatment implements Serializable {

    private Long id;

    private Long userId;

    private String processInstanceId;

    private AppointmentStatus status;

    public enum AppointmentStatus {
        ACTIVE, COMPLETED, CANCELED
    }

    private String type;

    private Date date;

    private Time time;

    private boolean next;

    private  String notes;

    public Treatment(Long userId, String type, Date date, Time time, String notes, AppointmentStatus status) {
        this.userId = userId;
        this.status = status;
        this.type = type;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }
    public Treatment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
