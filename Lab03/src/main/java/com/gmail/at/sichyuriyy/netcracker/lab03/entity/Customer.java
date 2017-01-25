package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Customer extends User {

    private List<Project> orderedProjects;

    private DatabaseConnector databaseConnector;


    public List<Project> getOrderedProjects() {
        if (orderedProjects == null) {
            databaseConnector.getProjectDao().findByCustomerId(this.id);
        }
        return orderedProjects;
    }

    public void setOrderedProjects(List<Project> orderedProjects) {
        this.orderedProjects = orderedProjects;
    }


}
