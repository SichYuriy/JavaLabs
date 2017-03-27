package com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;

/**
 * Created by Yuriy on 25.02.2017.
 */
public class MyDatabaseConnector implements DatabaseConnector {

    private MyDatabaseProjectDao projectDao;
    private MyDatabaseSprintDao sprintDao;
    private MyDatabaseTaskDao taskDao;
    private MyDatabaseTimeRequestDao timeRequestDao;
    private MyDatabaseUserDao userDao;
    private MyDatabaseCustomerDao customerDao;
    private MyDatabaseEmployeeDao employeeDao;
    private MyDatabaseProjectManagerDao projectManagerDao;
    private MyDatabaseTaskConfirmationDao taskConfirmationDao;

    public MyDatabaseConnector(MyDatabaseDaoFactory daoFactory) {
        createDao(daoFactory);
        initDaoDependencies();
    }

    @Override
    public ProjectDao getProjectDao() {
        return projectDao;
    }

    @Override
    public SprintDao getSprintDao() {
        return sprintDao;
    }

    @Override
    public TaskDao getTaskDao() {
        return taskDao;
    }

    @Override
    public TimeRequestDao getTimeRequestDao() {
        return timeRequestDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    @Override
    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    @Override
    public TaskConfirmationDao getTaskConfirmationDao() {
        return taskConfirmationDao;
    }

    @Override
    public ProjectManagerDao getProjectManagerDao() {
        return projectManagerDao;
    }

    private void createDao(MyDatabaseDaoFactory daoFactory) {
        customerDao = daoFactory.getCustomerDao();
        employeeDao = daoFactory.getEmployeeDao();
        projectDao = daoFactory.getProjectDao();
        projectManagerDao = daoFactory.getProjectManagerDao();
        sprintDao = daoFactory.getSprintDao();
        taskConfirmationDao = daoFactory.getTaskConfirmationDao();
        taskDao = daoFactory.getTaskDao();
        timeRequestDao = daoFactory.getTimeRequestDao();
        userDao = daoFactory.getUserDao();
    }

    private void initDaoDependencies() {
        initCustomerDaoDependencies();
        initEmployeeDaoDependencies();
        initProjectDaoDependencies();
        initProjectManagerDependencies();
        initSprintDaoDependencies();
        initTaskDaoDependencies();
        initTaskConfirmationDaoDependencies();
        initTimeRequestDaoDependencies();
        initUserDaoDependencies();
    }

    private void initCustomerDaoDependencies() {
        customerDao.setProjectDao(projectDao);
        customerDao.setUserDao(userDao);
    }

    private void initEmployeeDaoDependencies() {
        employeeDao.setUserDao(userDao);
        employeeDao.setTaskDao(taskDao);
        employeeDao.setTaskConfirmationDao(taskConfirmationDao);
        employeeDao.setTimeRequestDao(timeRequestDao);
    }

    private void initProjectDaoDependencies() {
        projectDao.setCustomerDao(customerDao);
        projectDao.setProjectManagerDao(projectManagerDao);
        projectDao.setSprintDao(sprintDao);
    }

    private void initProjectManagerDependencies() {
        projectManagerDao.setTaskConfirmationDao(taskConfirmationDao);
        projectManagerDao.setEmployeeDao(employeeDao);
        projectManagerDao.setProjectDao(projectDao);
        projectManagerDao.setTaskDao(taskDao);
        projectManagerDao.setUserDao(userDao);
    }

    private void initSprintDaoDependencies() {
        sprintDao.setTaskDao(taskDao);
        sprintDao.setProjectDao(projectDao);
    }

    private void initTaskConfirmationDaoDependencies() {
        taskConfirmationDao.setEmployeeDao(employeeDao);
        taskConfirmationDao.setTaskDao(taskDao);
    }

    private void initTaskDaoDependencies() {
        taskDao.setEmployeeDao(employeeDao);
        taskDao.setSprintDao(sprintDao);
        taskDao.setTaskConfirmationDao(taskConfirmationDao);
        taskDao.setTimeRequestDao(timeRequestDao);
    }

    private void initTimeRequestDaoDependencies() {
        timeRequestDao.setTaskDao(taskDao);
        timeRequestDao.setProjectManagerDao(projectManagerDao);
    }

    private void initUserDaoDependencies() {
        userDao.setProjectManagerDao(projectManagerDao);
        userDao.setEmployeeDao(employeeDao);
        userDao.setCustomerDao(customerDao);
    }


}
