package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper.CustomerMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.CustomerProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 30.01.2017.
 */
public class MyDatabaseCustomerDao implements CustomerDao {

    private static final String USER_TABLE_NAME = "User";
    private static final String CUSTOMER_TABLE_NAME = "Customer";
    private static final String PROJECT_TABLE_NAME = "Project";

    private CustomerMapper customerMapper = new CustomerMapper();

    private Database database;

    private UserDao userDao;
    private ProjectDao projectDao;

    @Override
    public void create(Customer customer) {
        List<Pair<String, Object>> values = new ArrayList<>();

        List<Long> roleIdList = customer.getRoles().stream()
                .map((r) -> (long) r.ordinal())
                .collect(Collectors.toList());

        values.add(new Pair<>("firstName", customer.getFirstName()));
        values.add(new Pair<>("lastName", customer.getLastName()));
        values.add(new Pair<>("login", customer.getLogin()));
        values.add(new Pair<>("password", customer.getPassword()));
        values.add(new Pair<>("roles", roleIdList));

        Long generatedUserId = database.insertInto(USER_TABLE_NAME, values);

        List<Pair<String, Object>> customerValues = new ArrayList<>();
        customerValues.add(new Pair<>("userId_extend", generatedUserId));
        database.insertInto(CUSTOMER_TABLE_NAME, customerValues);

        customer.setId(generatedUserId);
    }

    @Override
    public Customer findById(Long id) {
        Record userRecord = database.selectFrom(USER_TABLE_NAME, id);
        List<Record> customerRecords = database.selectFrom(CUSTOMER_TABLE_NAME,
                "userId_extend", id);
        if (userRecord == null
                || customerRecords == null
                || customerRecords.size() == 0) {
            return null;
        }
        Record customerRecord = customerRecords.get(0);

        Record join = userRecord.join(customerRecord, "User", "Customer");
        return parseRecord(join);
    }

    @Override
    public List<Customer> findAll() {
        List<Record> customerRecords = database.selectFrom(CUSTOMER_TABLE_NAME);
        return customerRecords.stream()
                .map((r) -> {
                    Long userRecordId = r.getLong("userId_extend");
                    Record userRecord = database.selectFrom(USER_TABLE_NAME, userRecordId);
                    if (userRecord == null) {
                        return null;
                    }
                    return  userRecord.join(r, "User", "Customer");
                })
                .filter(Objects::nonNull)
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Customer customer) {
        userDao.update(customer);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(CUSTOMER_TABLE_NAME,
                "userId_extend", id);
        database.selectFrom(USER_TABLE_NAME, id);

    }

    @Override
    public Customer findByProjectId(Long id) {
        Record projectRecord = database.selectFrom(PROJECT_TABLE_NAME, id);
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

}
