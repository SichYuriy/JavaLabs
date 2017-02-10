package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Employee extends User {

    public enum EmployeePosition {
        JUNIOR, MIDDLE, SENIOR
    }

    private List<Task> tasks;
    private List<TimeRequest> timeRequests;
    private EmployeePosition position;
    private List<TaskConfirmation> taskConfirmations;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<TimeRequest> getTimeRequests() {
        return timeRequests;
    }

    public void setTimeRequests(List<TimeRequest> timeRequests) {
        this.timeRequests = timeRequests;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    public List<TaskConfirmation> getTaskConfirmations() {
        return taskConfirmations;
    }

    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        this.taskConfirmations = taskConfirmations;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Employee{" +
                "tasks=" + tasks +
                ", timeRequests=" + timeRequests +
                ", position=" + position +
                ", taskConfirmations=" + taskConfirmations +
                '}';
    }
}
