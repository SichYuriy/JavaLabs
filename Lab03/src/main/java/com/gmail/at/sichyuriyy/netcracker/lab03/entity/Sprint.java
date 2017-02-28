package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Sprint implements Serializable {

    private Long id;
    private String name;
    private Project project;
    private List<Task> tasks;
    private Boolean finished;
    private Date plannedStartDate;
    private Date plannedEndDate;
    private Date startDate;
    private Date endDate;
    private Sprint previousSprint;
    private Sprint nextSprint;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Sprint getPreviousSprint() {
        return previousSprint;
    }

    public void setPreviousSprint(Sprint previousSprint) {
        this.previousSprint = previousSprint;
    }

    public Sprint getNextSprint() {
        return nextSprint;
    }

    public void setNextSprint(Sprint nextSprint) {
        this.nextSprint = nextSprint;
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", project=" + project +
                ", tasks=" + tasks +
                ", finished=" + finished +
                ", plannedStartDate=" + plannedStartDate +
                ", plannedEndDate=" + plannedEndDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
