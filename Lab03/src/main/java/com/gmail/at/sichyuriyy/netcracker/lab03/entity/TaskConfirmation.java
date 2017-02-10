package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

/**
 * Created by Yuriy on 25.01.2017.
 */
public class TaskConfirmation {

    public enum ConfirmationStatus {
        CONFIRMED, UNCONFIRMED
    }

    private Long id;
    private Employee employee;
    private Task task;
    private ConfirmationStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ConfirmationStatus getStatus() {
        return status;
    }

    public void setStatus(ConfirmationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskConfirmation{" +
                "id=" + id +
                ", employee=" + employee +
                ", task=" + task +
                ", status=" + status +
                '}';
    }
}
