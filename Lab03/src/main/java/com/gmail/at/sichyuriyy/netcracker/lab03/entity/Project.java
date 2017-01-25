package com.gmail.at.sichyuriyy.netcracker.lab03.entity;

import com.gmail.at.sichyuriyy.netcracker.lab03.DatabaseConnector.DatabaseConnector;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by Yuriy on 21.01.2017.
 */
public class Project implements Serializable {

    private Long id;
    private Employee projectManager;
    private Customer customer;
    private List<Sprint> sprints;
    private Date startDate;
    private Date endDate;

    private DatabaseConnector databaseConnector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getProjectManager() {
        if (this.projectManager == null) {
            projectManager = databaseConnector.getEmployeeDao().findManagerByProjectId(id);
        }
        return projectManager;
    }

    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    public Customer getCustomer() {
        if (customer == null) {
            customer = databaseConnector.getCustomerDao().findByProjectId(id);
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Sprint> getSprints() {
        if (sprints == null) {
            sprints = databaseConnector.getSprintDao().findByProjectId(id);
        }
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
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
