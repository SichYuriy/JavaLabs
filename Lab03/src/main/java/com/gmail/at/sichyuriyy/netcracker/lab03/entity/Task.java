package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import java.util.List;
import java.util.zip.CheckedInputStream;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Task {

    public enum TaskStatus {
        PLANED, WORKING, FINISHED
    }

    private Long id;
    private List<Employee> employees;
    private Integer estimateTime;
    private Integer executionTime;
    private List<Task> childTasks;
    private List<Task> dependencies;
    private Employee.EmployeePosition requiredPosition;
    private TaskStatus status;
    private List<TaskConfirmation> taskConfirmations;


    private DatabaseConnector databaseConnector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        if (employees == null) {
            employees = databaseConnector.getEmployeeDao().findByTaskId(id);
        }
        this.employees = employees;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public List<Task> getChildTasks() {
        if (childTasks == null) {
            childTasks = databaseConnector.getTaskDao()
                    .findChildTasksByParentTaskId(id);
        }
        return childTasks;
    }

    public void setChildTasks(List<Task> childTasks) {
        this.childTasks = childTasks;
    }

    public List<Task> getDependencies() {
        if (dependencies == null) {
            dependencies = databaseConnector.getTaskDao()
                    .findDependenciesByParentTaskId(id);
        }
        return dependencies;
    }

    public void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public Employee.EmployeePosition getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(Employee.EmployeePosition requiredPosition) {
        this.requiredPosition = requiredPosition;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<TaskConfirmation> getTaskConfirmations() {
        if (taskConfirmations == null) {
            taskConfirmations = databaseConnector.getTaskConfirmationDao()
                    .findByTaskId(id);
        }
        return taskConfirmations;
    }

    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        this.taskConfirmations = taskConfirmations;
    }
}
