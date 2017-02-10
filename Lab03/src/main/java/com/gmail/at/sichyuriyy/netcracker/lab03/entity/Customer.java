package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Customer extends User {

    private List<Project> orderedProjects;

    public List<Project> getOrderedProjects() {
        return orderedProjects;
    }

    public void setOrderedProjects(List<Project> orderedProjects) {
        this.orderedProjects = orderedProjects;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Customer {" +
                "orderedProjects=" + orderedProjects +
                '}';
    }
}
