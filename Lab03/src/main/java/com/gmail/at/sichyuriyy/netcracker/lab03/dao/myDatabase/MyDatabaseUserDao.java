package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.User;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class MyDatabaseUserDao implements UserDao {

    private static final String USER_TABLE_NAME = "User";
    private static final String USER_ROLE_TABLE_NAME = "user_role";
    private static final String ROLE_TABLE_NAME = "Role";

    private Database database;

    private EmployeeDao employeeDao;
    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;

    @Override
    public User findById(Long id) {
        User user = projectManagerDao.findById(id);
        if (user != null)
            return user;
        user = employeeDao.findById(id);
        if (user != null)
            return user;
        return customerDao.findById(id);
    }

    @Override
    public void update(User user) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("firstName", user.getFirstName()));
        values.add(new Pair<>("lastName", user.getLastName()));
        values.add(new Pair<>("login", user.getLogin()));
        values.add(new Pair<>("password", user.getPassword()));

        database.update(USER_TABLE_NAME, user.getId(), values);

    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        List<Record> roleIdRecords = database.selectFrom(USER_ROLE_TABLE_NAME,
                "userId", userId);
        return roleIdRecords.stream()
                .map((roleIdRecord) -> roleIdRecord.getLong("roleId"))
                .map((roleId) -> database.selectFrom(ROLE_TABLE_NAME, roleId))
                .map((roleRecord) -> roleRecord.getString("name"))
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRoles(Long userId, List<Role> roles) {
        deleteAllRoles(userId);
        addRoles(userId, roles);
    }

    @Override
    public void addRole(Long userId, Role role) {
        Long roleId = findRoleIdByName(role.toString());
        if (roleId == null) {
            //TODO: log error
            return;
        }
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("userId", userId));
        values.add(new Pair<>("roleId", roleId));
        database.insertInto(USER_ROLE_TABLE_NAME, values);
    }

    @Override
    public void addRoles(Long userId, List<Role> roles) {
        roles.forEach((role) -> addRole(userId, role));
    }

    @Override
    public void deleteRole(Long userId, Role role) {
        Long roleId = findRoleIdByName(role.toString());
        if (roleId == null) {
            //TODO: log error
            return;
        }
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("userId", userId));
        filters.add(new Pair<>("roleId", roleId));

    }

    @Override
    public void deleteAllRoles(Long userId) {
        database.deleteFrom(USER_ROLE_TABLE_NAME,
                "userId", userId);
    }

    private Long findRoleIdByName(String name) {
        List<Record> roleRecords = database.selectFrom(ROLE_TABLE_NAME,
                "name", name);
        if (roleRecords.size() == 0) {
            return null;
        }
        return roleRecords.get(0).getLong("id");
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public ProjectManagerDao getProjectManagerDao() {
        return projectManagerDao;
    }

    public void setProjectManagerDao(ProjectManagerDao projectManagerDao) {
        this.projectManagerDao = projectManagerDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }
}
