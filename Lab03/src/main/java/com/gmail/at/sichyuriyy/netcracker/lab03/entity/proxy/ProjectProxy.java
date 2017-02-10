package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class ProjectProxy extends Project {

    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;
    private SprintDao sprintDao;

    private boolean managerLoaded;
    private boolean customerLoaded;
    private boolean sprintsLoaded;

    private Long managerId;
    private Long customerId;

    public ProjectProxy(ProjectManagerDao projectManagerDao, CustomerDao customerDao,
                        SprintDao sprintDao) {
        this.projectManagerDao = projectManagerDao;
        this.customerDao = customerDao;
        this.sprintDao = sprintDao;
    }

    @Override
    public ProjectManager getProjectManager() {
        if (!managerLoaded) {
            loadManager();
        }
        return super.getProjectManager();
    }

    @Override
    public void setProjectManager(ProjectManager projectManager) {
        managerLoaded = true;
        super.setProjectManager(projectManager);
    }

    @Override
    public Customer getCustomer() {
        if (!customerLoaded) {
            loadCustomer();
        }
        return super.getCustomer();
    }

    @Override
    public void setCustomer(Customer customer) {
        customerLoaded = true;
        super.setCustomer(customer);
    }

    @Override
    public List<Sprint> getSprints() {
        if (!sprintsLoaded) {
            loadSprints();
        }
        return super.getSprints();
    }

    @Override
    public void setSprints(List<Sprint> sprints) {
        sprintsLoaded = true;
        super.setSprints(sprints);
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private void loadManager() {
        ProjectManager manager;
        if (managerId != null) {
            manager = projectManagerDao.findById(managerId);
        } else {
            manager = projectManagerDao.findByProjectId(getId());
        }
        this.setProjectManager(manager);
    }

    private void loadCustomer() {
        Customer customer;
        if (customerId != null) {
            customer = customerDao.findById(customerId);
        } else {
            customer = customerDao.findByProjectId(getId());
        }
        this.setCustomer(customer);
    }

    private void loadSprints() {
        this.setSprints(sprintDao.findByProjectId(getId()));
    }
}
