package com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface DatabaseConnector {

    ProjectDao getProjectDao();
    SprintDao getSprintDao();
    TaskDao getTaskDao();
    TimeRequestDao getTimeRequestDao();
    UserDao getUserDao();
    CustomerDao getCustomerDao();
    EmployeeDao getEmployeeDao();
    TaskConfirmationDao getTaskConfirmationDao();
    ProjectManagerDao getProjectManagerDao();


}
