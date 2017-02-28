package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.RelationUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 26.02.2017.
 */
public abstract class UserDaoTest {

    private DatabaseConnector databaseConnector;
    private UserDao userDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        userDao = databaseConnector.getUserDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }
    
    @Test
    public void findByIdEmployee() {
        Employee employee = TestData.getEmployee();
        databaseConnector.getEmployeeDao().create(employee);
        
        User dbUser = userDao.findById(employee.getId());
        assertWeakEquals(employee, dbUser);
    }


    @Test
    public void findByIdCustomer() {
        Customer customer = TestData.getCustomer();
        databaseConnector.getCustomerDao().create(customer);

        User dbUser = userDao.findById(customer.getId());
        assertWeakEquals(customer, dbUser);
    }


    @Test
    public void findByIdProjectManager() {
        ProjectManager projectManager = TestData.getProjectManager();
        databaseConnector.getProjectManagerDao().create(projectManager);

        User dbUser = userDao.findById(projectManager.getId());
        assertWeakEquals(projectManager, dbUser);
    }

    @Test
    public void updateRoles() {
        Employee employee = TestData.getEmployee();
        databaseConnector.getEmployeeDao().create(employee);

        RelationUtils.addRoles(employee, Role.ADMINISTRATOR, Role.CUSTOMER);
        userDao.updateRoles(employee.getId(), employee.getRoles());

        List<Role> expected = new ArrayList<>();
        expected.add(Role.EMPLOYEE);
        expected.add(Role.ADMINISTRATOR);
        expected.add(Role.CUSTOMER);

        List<Role> actual = userDao.findRolesByUserId(employee.getId());
        assertTrue(TestUtils.equalContentCollections(expected, actual));
    }

    @Test
    public void addRole() {
        Employee employee = TestData.getEmployee();
        databaseConnector.getEmployeeDao().create(employee);

        userDao.addRole(employee.getId(), Role.ADMINISTRATOR);

        List<Role> expected = new ArrayList<>();
        expected.add(Role.EMPLOYEE);
        expected.add(Role.ADMINISTRATOR);

        List<Role> actual = userDao.findRolesByUserId(employee.getId());
        assertTrue(TestUtils.equalContentCollections(expected, actual));
    }

    @Test
    public void addRoles() {
        Employee employee = TestData.getEmployee();
        databaseConnector.getEmployeeDao().create(employee);

        List<Role> addRoles = new ArrayList<>();
        addRoles.add(Role.ADMINISTRATOR);
        addRoles.add(Role.CUSTOMER);

        userDao.addRoles(employee.getId(), addRoles);

        List<Role> expected = new ArrayList<>();
        expected.add(Role.EMPLOYEE);
        expected.add(Role.ADMINISTRATOR);
        expected.add(Role.CUSTOMER);

        List<Role> actual = userDao.findRolesByUserId(employee.getId());
        assertTrue(TestUtils.equalContentCollections(expected, actual));
    }

    @Test
    public void deleteRole() {
        ProjectManager projectManager = TestData.getProjectManager();
        databaseConnector.getEmployeeDao().create(projectManager);

        userDao.deleteRole(projectManager.getId(), Role.EMPLOYEE);

        List<Role> expected = new ArrayList<>();
        expected.add(Role.PROJECT_MANAGER);

        List<Role> actual = userDao.findRolesByUserId(projectManager.getId());
        assertTrue(TestUtils.equalContentCollections(expected, actual));
    }

    @Test
    public void deleteAllRoles() throws Exception {
        ProjectManager projectManager = TestData.getProjectManager();
        databaseConnector.getEmployeeDao().create(projectManager);

        userDao.deleteAllRoles(projectManager.getId());

        List<Role> dbRoles = userDao.findRolesByUserId(projectManager.getId());
        assertTrue(dbRoles.isEmpty());
    }
    
    private void assertWeakEquals(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getLogin(), actual.getLogin());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertTrue(TestUtils.equalContentCollections(expected.getRoles(), actual.getRoles()));
    }

}