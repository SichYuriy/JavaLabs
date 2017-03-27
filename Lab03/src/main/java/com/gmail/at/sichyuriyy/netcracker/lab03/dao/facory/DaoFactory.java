package com.gmail.at.sichyuriyy.netcracker.lab03.dao.facory;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;

/**
 * Created by Yuriy on 3/20/2017.
 */
public interface DaoFactory {

    CustomerDao getCustomerDao();
    EmployeeDao getEmployeeDao();
    ProjectDao getProjectDao();
    ProjectManagerDao getProjectManagerDao();
    SprintDao getSprintDao();
    TaskConfirmationDao getTaskConfirmationDao();
    TaskDao getTaskDao();
    TimeRequestDao getTimeRequestDao();
    UserDao getUserDao();
}
