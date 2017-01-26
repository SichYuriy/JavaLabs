package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import java.util.List;

/**
 * Created by Yuriy on 26.01.2017.
 */
public class ProjectManager extends User {

    private List<Project> managedProjects;

    private DatabaseConnector databaseConnector;

    public List<Project> getManagedProjects() {
        if (managedProjects == null) {
            managedProjects = databaseConnector.getProjectDao().findByManagerId(id);
        }
        return managedProjects;
    }

    public void setManagedProjects(List<Project> managedProjects) {
        this.managedProjects = managedProjects;
    }

}
