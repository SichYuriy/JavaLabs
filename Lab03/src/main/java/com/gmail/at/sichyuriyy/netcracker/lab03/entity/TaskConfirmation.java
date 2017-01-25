package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

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

    private DatabaseConnector databaseConnector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        if (employee == null) {
            employee = databaseConnector.getEmployeeDao().findByTaskConfirmationId(id);
        }
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task getTask() {
        if (task == null) {
            task = databaseConnector.getTaskDao().findByTaskConfirmationId(id);
        }
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
}
