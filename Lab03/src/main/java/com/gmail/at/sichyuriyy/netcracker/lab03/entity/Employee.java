package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

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

    private int managedProjectsCount;
    private List<Project> managedProjects;

    private DatabaseConnector databaseConnector;

    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = databaseConnector.getTaskDao().findByEmployeeId(id);
        }
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<TimeRequest> getTimeRequests() {
        if (timeRequests == null) {
            timeRequests = databaseConnector.getTimeRequestDao().findByEmployeeId(id);
        }
        return timeRequests;
    }

    public void setTimeRequests(List<TimeRequest> timeRequests) {
        this.timeRequests = timeRequests;
    }

    public int getManagedProjectsSize() {
        return managedProjectsCount;
    }

    public void setManagedProjectsSize(int managedProjectsCount) {
        this.managedProjectsCount = managedProjectsCount;
    }

    public List<Project> getManagedProjects() {
        if (managedProjects == null) {
            managedProjects = databaseConnector.getProjectDao().findByManagerId(id);
        }
        return managedProjects;
    }

    public void setManagedProjects(List<Project> managedProjects) {
        this.managedProjects = managedProjects;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    public List<TaskConfirmation> getTaskConfirmations() {
        if (taskConfirmations == null) {
            taskConfirmations = databaseConnector.getTaskConfirmationDao()
                    .findByEmployeeId(id);
        }
        return taskConfirmations;
    }

    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        this.taskConfirmations = taskConfirmations;
    }

    public int getManagedProjectsCount() {
        return managedProjectsCount;
    }

    public void setManagedProjectsCount(int managedProjectsCount) {
        this.managedProjectsCount = managedProjectsCount;
    }
}
