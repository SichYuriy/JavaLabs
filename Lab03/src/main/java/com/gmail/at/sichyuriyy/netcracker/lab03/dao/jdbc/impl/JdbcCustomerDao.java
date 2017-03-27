package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.JdbcTemplate;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.CustomerExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.PropertiesLoader;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * Created by Yuriy on 3/19/2017.
 */
public class JdbcCustomerDao extends AbstractJdbcDao implements CustomerDao {

    private JdbcUserDao userDao;
    private JdbcProjectDao projectDao;

    public JdbcCustomerDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(Customer customer) {
        userDao.createUser(customer,
                Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.USER_TYPE)));
    }

    @Override
    public Customer findById(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_ID, new CustomerExtractor(projectDao, userDao),
                id, metamodelProperties.getProperty(MetamodelProperties.USER_TYPE)
        );
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID, new CustomerExtractor(projectDao, userDao),
                metamodelProperties.getProperty(MetamodelProperties.ROLE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ROLE),
                metamodelProperties.getProperty(MetamodelProperties.ROLES),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE)
        );
    }

    @Override
    public void update(Customer customer) {
        userDao.update(customer);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Customer findByProjectId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID, new CustomerExtractor(projectDao, userDao),
                metamodelProperties.get(MetamodelProperties.PROJECT_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ID),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    public JdbcUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    public JdbcProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(JdbcProjectDao projectDao) {
        this.projectDao = projectDao;
    }
}
