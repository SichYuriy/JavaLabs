package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.io.Serializable;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class TimeRequest {

    public enum Status implements Serializable {
        ACCEPTED, DENIED, PENDING
    }

    private Long id;
    private Task task;
    private Integer requestTime;
    private Integer responseTime;
    private Status status;
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Integer requestTime) {
        this.requestTime = requestTime;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "TimeRequest{" +
                "id=" + id +
                ", task=" + task +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
