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
    private Date plannedStartDate;
    private Date plannedEndDate;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (id != null ? !id.equals(project.id) : project.id != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null)
            return false;
        if (projectManager != null ? !projectManager.equals(project.projectManager) : project.projectManager != null)
            return false;
        if (customer != null ? !customer.equals(project.customer) : project.customer != null)
            return false;
        if (plannedStartDate != null ? !plannedStartDate.equals(project.plannedStartDate) : project.plannedStartDate != null)
            return false;
        if (plannedEndDate != null ? !plannedEndDate.equals(project.plannedEndDate) : project.plannedEndDate != null)
            return false;
        if (startDate != null ? !startDate.equals(project.startDate) : project.startDate != null)
            return false;
        return endDate != null ? endDate.equals(project.endDate) : project.endDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (projectManager != null ? projectManager.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (plannedStartDate != null ? plannedStartDate.hashCode() : 0);
        result = 31 * result + (plannedEndDate != null ? plannedEndDate.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
