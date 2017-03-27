package com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.factory.JdbcDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcDatabaseConnector implements DatabaseConnector {
    private JdbcProjectDao projectDao;
    private JdbcSprintDao sprintDao;
    private JdbcTaskDao taskDao;
    private JdbcTimeRequestDao timeRequestDao;
    private JdbcUserDao userDao;
    private JdbcCustomerDao customerDao;
    private JdbcEmployeeDao employeeDao;
    private JdbcProjectManagerDao projectManagerDao;
    private JdbcTaskConfirmationDao taskConfirmationDao;

    public JdbcDatabaseConnector(JdbcDaoFactory daoFactory) {
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

    private void createDao(JdbcDaoFactory daoFactory) {
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
