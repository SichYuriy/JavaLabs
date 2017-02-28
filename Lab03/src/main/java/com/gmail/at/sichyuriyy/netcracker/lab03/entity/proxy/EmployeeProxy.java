package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskConfirmationDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TimeRequestDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class EmployeeProxy extends Employee {

    private TaskDao taskDao;
    private TimeRequestDao timeRequestDao;
    private TaskConfirmationDao taskConfirmationDao;
    private UserDao userDao;

    private boolean tasksLoaded;
    private boolean timeRequestsLoaded;
    private boolean taskConfirmationsLoaded;
    private boolean rolesLoaded;

    public EmployeeProxy(TaskDao taskDao, TimeRequestDao timeRequestDao,
                         TaskConfirmationDao taskConfirmationDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.timeRequestDao = timeRequestDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.userDao = userDao;
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
