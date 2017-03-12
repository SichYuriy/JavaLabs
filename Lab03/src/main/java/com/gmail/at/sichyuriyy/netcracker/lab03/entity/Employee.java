package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Employee extends User {

    public enum Position {
        JUNIOR, MIDDLE, SENIOR
    }

    private List<Task> tasks;
    private Position position;
    private List<TaskConfirmation> taskConfirmations;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
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
                ", position=" + position +
                '}';
    }
}
