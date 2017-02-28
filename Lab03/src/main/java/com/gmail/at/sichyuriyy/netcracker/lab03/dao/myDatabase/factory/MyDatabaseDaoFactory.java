package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.*;

/**
 * Created by Yuriy on 25.02.2017.
 */
public interface MyDatabaseDaoFactory {

    MyDatabaseCustomerDao getCustomerDao();
    MyDatabaseEmployeeDao getEmployeeDao();
    MyDatabaseProjectDao getProjectDao();
    MyDatabaseProjectManagerDao getProjectManagerDao();
    MyDatabaseSprintDao getSprintDao();
    MyDatabaseTaskConfirmationDao getTaskConfirmationDao();
    MyDatabaseTaskDao getTaskDao();
    MyDatabaseTimeRequestDao getTimeRequestDao();
    MyDatabaseUserDao getUserDao();
}
