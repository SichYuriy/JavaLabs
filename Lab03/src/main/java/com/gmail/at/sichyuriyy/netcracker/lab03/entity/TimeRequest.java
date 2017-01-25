package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import java.io.Serializable;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class TimeRequest {

    public enum Status implements Serializable {
        ACCEPTED, DENIED, PENDING
    }

    private Long id;
    private Employee employee;
    private Task task;
    private int requestTime;
    private int responseTime;
    private Status status;
    private String reason;

    private DatabaseConnector databaseConnector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        if (employee == null) {
            employee = databaseConnector.getEmployeeDao().findByTimeRequestId(id);
        }
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task getTask() {
        if (task == null) {
            databaseConnector.getTaskDao().findByTimeRequestId(id);
        }
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(int requestTime) {
        this.requestTime = requestTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeRequest that = (TimeRequest) o;

        if (requestTime != that.requestTime) return false;
        if (responseTime != that.responseTime) return false;
        if (!id.equals(that.id)) return false;
        if (!employee.equals(that.employee)) return false;
        if (!task.equals(that.task)) return false;
        if (status != that.status) return false;
        return reason != null ? reason.equals(that.reason) : that.reason == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + employee.hashCode();
        result = 31 * result + task.hashCode();
        result = 31 * result + requestTime;
        result = 31 * result + responseTime;
        result = 31 * result + status.hashCode();
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimeRequest{" +
                "id=" + id +
                ", employee=" + employee +
                ", task=" + task +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
