package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.CustomerMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.CustomerProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 30.01.2017.
 */
public class MyDatabaseCustomerDao implements CustomerDao {

    private static final String USER_TABLE_NAME = "User";
    private static final String PROJECT_TABLE_NAME = "Project";
    private static final String ROLE_TABLE_NAME = "Role";
    private static final String USER_ROLE_TABLE_NAME = "user_role";


    private CustomerMapper customerMapper = new CustomerMapper();

    private Database database;

    private UserDao userDao;
    private ProjectDao projectDao;

    public MyDatabaseCustomerDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(Customer customer) {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>("firstName", customer.getFirstName()));
        values.add(new Pair<>("lastName", customer.getLastName()));
        values.add(new Pair<>("login", customer.getLogin()));
        values.add(new Pair<>("password", customer.getPassword()));

        Long generatedUserId = database.insertInto(USER_TABLE_NAME, values);

        userDao.addRoles(generatedUserId, customer.getRoles());

        customer.setId(generatedUserId);
    }

    @Override
    public Customer findById(Long id) {
        Record customer = database.selectFrom(USER_TABLE_NAME, id);
        if (customer == null) {
            return null;
        }
        List<Role> roles = userDao.findRolesByUserId(id);
        if (!roles.contains(Role.CUSTOMER)) {
            return null;
        }
        return parseRecord(customer);
    }

    @Override
    public List<Customer> findAll() {
        Long roleId = findRoleIdByName(Role.CUSTOMER.toString());

        List<Record> customerIdRecords = database.selectFrom(USER_ROLE_TABLE_NAME,
                "roleId", roleId);
        return customerIdRecords.stream()
                .map(record -> record.getLong("userId"))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Customer customer) {
        userDao.update(customer);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(USER_TABLE_NAME, id);
    }

    @Override
    public Customer findByProjectId(Long id) {
        Record projectRecord = database.selectFrom(PROJECT_TABLE_NAME, id);
        if (projectRecord == null) {
            return null;
        }
        Long customerId = projectRecord.getLong("customerId");
        return customerId != null ? findById(customerId) : null;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    private Customer parseRecord(Record record) {
        Customer customer = new CustomerProxy(projectDao, userDao);
        customerMapper.map(customer, record);
        return customer;
    }

    private Long findRoleIdByName(String name) {
        List<Record> roleRecords = database.selectFrom(ROLE_TABLE_NAME,
                "name", name);
        if (roleRecords.size() == 0) {
            return null;
        }
        return roleRecords.get(0).getLong("id");
    }

}
