package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.facory.DaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;

/**
 * Created by Yuriy on 25.02.2017.
 */
public class MyDatabaseDaoFactory implements DaoFactory {

    private Database database;

    public MyDatabaseDaoFactory(Database database) {
        this.database = database;
    }

    @Override
    public MyDatabaseCustomerDao getCustomerDao() {
        return new MyDatabaseCustomerDao(database);
    }

    @Override
    public MyDatabaseEmployeeDao getEmployeeDao() {
        return new MyDatabaseEmployeeDao(database);
    }

    @Override
    public MyDatabaseProjectDao getProjectDao() {
        return new MyDatabaseProjectDao(database);
    }

    @Override
    public MyDatabaseProjectManagerDao getProjectManagerDao() {
        return new MyDatabaseProjectManagerDao(database);
    }

    @Override
    public MyDatabaseSprintDao getSprintDao() {
        return new MyDatabaseSprintDao(database);
    }

    @Override
    public MyDatabaseTaskConfirmationDao getTaskConfirmationDao() {
        return new MyDatabaseTaskConfirmationDao(database);
    }

    @Override
    public MyDatabaseTaskDao getTaskDao() {
        return new MyDatabaseTaskDao(database);
    }

    @Override
    public MyDatabaseTimeRequestDao getTimeRequestDao() {
        return new MyDatabaseTimeRequestDao(database);
    }

    @Override
    public MyDatabaseUserDao getUserDao() {
        return new MyDatabaseUserDao(database);
    }
}
