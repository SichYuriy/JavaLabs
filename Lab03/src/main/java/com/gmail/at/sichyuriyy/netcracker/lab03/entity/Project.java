package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Project implements Serializable {

    private Long id;
    private String name;
    private ProjectManager projectManager;
    private Customer customer;
    private List<Sprint> sprints;
    private Date planedStartDate;
    private Date planedEndDate;
    private Date startDate;
    private Date endDate;


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

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public Date getPlanedStartDate() {
        return planedStartDate;
    }

    public void setPlanedStartDate(Date planedStartDate) {
        this.planedStartDate = planedStartDate;
    }

    public Date getPlanedEndDate() {
        return planedEndDate;
    }

    public void setPlanedEndDate(Date planedEndDate) {
        this.planedEndDate = planedEndDate;
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


}
