package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class CustomerProxy extends Customer {

    private ProjectDao projectDao;
    private UserDao userDao;

    private boolean orderedProjectsLoaded;
    private boolean rolesLoaded;

    public CustomerProxy(ProjectDao projectDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    @Override
    public List<Project> getOrderedProjects() {
        if (!orderedProjectsLoaded) {
            loadOrderedProjects();
        }
        return super.getOrderedProjects();
    }

    @Override
    public void setOrderedProjects(List<Project> orderedProjects) {
        orderedProjectsLoaded = true;
        super.setOrderedProjects(orderedProjects);
    }

    @Override
    public List<Role> getRoles() {
        if (!rolesLoaded) {
            loadRoles();
        }
        return super.getRoles();
    }

    @Override
    public void setRoles(List<Role> roles) {
        rolesLoaded = true;
        super.setRoles(roles);
    }

    private void loadOrderedProjects() {
        this.setOrderedProjects(projectDao.findByCustomerId(getId()));
    }

    private void loadRoles() {
        this.setRoles(userDao.findRolesByUserId(getId()));
    }
}
