package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class ProjectManagerProxy extends ProjectManager {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private TaskConfirmationDao taskConfirmationDao;
    private UserDao userDao;

    private boolean managedProjectsLoaded;
    private boolean tasksLoaded;
    private boolean taskConfirmationsLoaded;
    private boolean rolesLoaded;

    public ProjectManagerProxy(ProjectDao projectDao,
                               TaskDao taskDao,
                               TaskConfirmationDao taskConfirmationDao,
                               UserDao userDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.userDao = userDao;
    }

    @Override
    public List<Project> getManagedProjects() {
        if (!managedProjectsLoaded) {
            loadMangedProjects();
        }
        return super.getManagedProjects();
    }

    @Override
    public void setManagedProjects(List<Project> managedProjects) {
        managedProjectsLoaded = true;
        super.setManagedProjects(managedProjects);
    }

    @Override
    public List<Task> getTasks() {
        if (!tasksLoaded) {
            loadTasks();
        }
        return super.getTasks();
    }

    @Override
    public void setTasks(List<Task> tasks) {
        tasksLoaded = true;
        super.setTasks(tasks);
    }

    @Override
    public List<TaskConfirmation> getTaskConfirmations() {
        if (!taskConfirmationsLoaded) {
            loadTaskConfirmations();
        }
        return super.getTaskConfirmations();
    }

    @Override
    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        taskConfirmationsLoaded = true;
        super.setTaskConfirmations(taskConfirmations);
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

    private void loadMangedProjects() {
        this.setManagedProjects(projectDao.findByManagerId(getId()));
    }

    private void loadTasks() {
        this.setTasks(taskDao.findByEmployeeId(getId()));
    }

    private void loadTaskConfirmations() {
        this.setTaskConfirmations(taskConfirmationDao.findByEmployeeId(getId()));
    }

    private void loadRoles() {
        this.setRoles(userDao.findRolesByUserId(getId()));
    }
}
