package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

/**
 * Created by Yuriy on 25.01.2017.
 */
public class TaskConfirmation {

    public enum Status {
        CONFIRMED, UNCONFIRMED
    }

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskConfirmation{" +
                ", status=" + status +
                '}';
    }
}
