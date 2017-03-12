package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Task {

    public enum Status {
        UNCONFIRMED, PLANED, WORKING, FINISHED
    }

    private Long id;
    private String name;
    private List<Employee> employees;
    private Integer estimateTime;
    private Integer executionTime;
    private Task parentTask;
    private List<Task> childTasks;
    private List<Task> dependencies;
    private Employee.Position requiredPosition;
    private Status status;
    private List<TaskConfirmation> taskConfirmations;
    private List<TimeRequest> timeRequests;
    private Sprint sprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
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

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public List<Task> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<Task> childTasks) {
        this.childTasks = childTasks;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public Employee.Position getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(Employee.Position requiredPosition) {
        this.requiredPosition = requiredPosition;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<TaskConfirmation> getTaskConfirmations() {
        return taskConfirmations;
    }

    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        this.taskConfirmations = taskConfirmations;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public List<TimeRequest> getTimeRequests() {
        return timeRequests;
    }

    public void setTimeRequests(List<TimeRequest> timeRequests) {
        this.timeRequests = timeRequests;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employees=" + employees +
                ", estimateTime=" + estimateTime +
                ", executionTime=" + executionTime +
                ", childTasks=" + childTasks +
                ", dependencies=" + dependencies +
                ", requiredPosition=" + requiredPosition +
                ", status=" + status +
                ", taskConfirmations=" + taskConfirmations +
                ", sprint=" + sprint +
                ", timeRequests=" + timeRequests +
                '}';
    }
}
