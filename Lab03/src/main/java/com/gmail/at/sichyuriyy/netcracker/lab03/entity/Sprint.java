package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Sprint implements Serializable {

    private Long id;
    private Project project;
    private List<Task> tasks;
    private Boolean finished;

    private DatabaseConnector databaseConnector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        if (project == null) {
            project = databaseConnector.getProjectDao().findBySprintId(id);
        }
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = databaseConnector.getTaskDao().findBySprintId(id);
        }
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
}
