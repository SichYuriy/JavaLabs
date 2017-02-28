package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 26.01.2017.
 */
public class ProjectManager extends Employee {

    private List<Project> managedProjects;

    public List<Project> getManagedProjects() {
        return managedProjects;
    }

    public void setManagedProjects(List<Project> managedProjects) {
        this.managedProjects = managedProjects;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ProjectManager{" +
                "managedProjects=" + ((managedProjects != null) ? managedProjects.stream().map(Project::getId).collect(Collectors.toList()) : null) +
                '}';
    }
}
