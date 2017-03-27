package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.factory;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.facory.DaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl.*;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcDaoFactory implements DaoFactory {

    private ConnectionManager connectionManager;

    public JdbcDaoFactory(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public JdbcCustomerDao getCustomerDao() {
        return new JdbcCustomerDao(connectionManager);
    }

    @Override
    public JdbcEmployeeDao getEmployeeDao() {
        return new JdbcEmployeeDao(connectionManager);
    }

    @Override
    public JdbcProjectDao getProjectDao() {
        return new JdbcProjectDao(connectionManager);
    }

    @Override
    public JdbcProjectManagerDao getProjectManagerDao() {
        return new JdbcProjectManagerDao(connectionManager);
    }

    @Override
    public JdbcSprintDao getSprintDao() {
        return new JdbcSprintDao(connectionManager);
    }

    @Override
    public JdbcTaskConfirmationDao getTaskConfirmationDao() {
        return new JdbcTaskConfirmationDao(connectionManager);
    }

    @Override
    public JdbcTaskDao getTaskDao() {
        return new JdbcTaskDao(connectionManager);
    }

    @Override
    public JdbcTimeRequestDao getTimeRequestDao() {
        return new JdbcTimeRequestDao(connectionManager);
    }

    @Override
    public JdbcUserDao getUserDao() {
        return new JdbcUserDao(connectionManager);
    }
}
