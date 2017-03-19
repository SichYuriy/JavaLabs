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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 26.02.2017.
 */
public abstract class EmployeeDaoTest {

    private DatabaseConnector databaseConnector;
    private EmployeeDao employeeDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        employeeDao = databaseConnector.getEmployeeDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Employee employee = TestData.getEmployee();
        employeeDao.create(employee);

        Employee dbEmployee = employeeDao.findById(employee.getId());
        assertWeakEquals(employee, dbEmployee);
    }

    @Test
    public void delete() {
        Employee employee = TestData.getEmployee();
        employeeDao.create(employee);
        assertNotNull(employeeDao.findById(employee.getId()));

        employeeDao.delete(employee.getId());
        assertNull(employeeDao.findById(employee.getId()));
    }

    @Test
    public void findAll() {
        Customer customer1 = TestData.getCustomer("customer1");
        Customer customer2 = TestData.getCustomer("customer2");
        Customer customer3 = TestData.getCustomer("customer3");
        Employee employee = TestData.getEmployee();
        ProjectManager projectManager = TestData.getProjectManager();

        databaseConnector.getCustomerDao().create(customer1);
        databaseConnector.getCustomerDao().create(customer2);
        databaseConnector.getCustomerDao().create(customer3);
        databaseConnector.getEmployeeDao().create(employee);
        databaseConnector.getProjectManagerDao().create(projectManager);

        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(employee);
        expectedList.add(projectManager);

        List<Employee> actualList = employeeDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expectedList,
                actualList,
                this::weakEquals)
        );

    }

    @Test
    public void update() {
        Employee employee = TestData.getEmployee();
        employeeDao.create(employee);
        employee.setLogin("updatedLogin");
        employee.setPassword("updatedPassword");
        employee.setFirstName("updatedFirstName");
        employee.setLastName("updatedLastName");
        employee.setPosition(Employee.Position.MIDDLE);
        employeeDao.update(employee);

        Employee dbEmployee = employeeDao.findById(employee.getId());
        assertWeakEquals(employee, dbEmployee);
    }

    @Test
    public void findByTaskId() {
        Employee employee1 = TestData.getEmployee("employee1");
        Employee employee2 = TestData.getEmployee("employee2");
        Employee employee3 = TestData.getEmployee("employee3");

        Customer customer = TestData.getCustomer();
        Project project = TestData.getProject(customer, null);
        Sprint sprint = TestData.getSprint(project);

        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectDao().create(project);
        databaseConnector.getSprintDao().create(sprint);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);

        employeeDao.create(employee1);
        employeeDao.create(employee2);
        employeeDao.create(employee3);

        databaseConnector.getTaskDao().addEmployee(task1.getId(), employee1);
        databaseConnector.getTaskDao().addEmployee(task1.getId(), employee2);
        databaseConnector.getTaskDao().addEmployee(task2.getId(), employee3);

        RelationUtils.addTaskEmployeeRelation(task1, employee1, employee2);
        RelationUtils.addTaskEmployeeRelation(task2, employee3);

        employee1.setTaskConfirmations(Collections.singletonList(TestData.getTaskConfirmation()));
        employee2.setTaskConfirmations(Collections.singletonList(TestData.getTaskConfirmation()));

        List<Employee> expected = new ArrayList<>();
        expected.add(employee1);
        expected.add(employee2);

        List<Employee> actual = employeeDao.findByTaskId(task1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void confirmTask() {
        Customer customer = TestData.getCustomer();
        Project project = TestData.getProject(customer, null);
        Sprint sprint = TestData.getSprint(project);

        Task task = TestData.getTask(sprint);
        Employee employee = TestData.getEmployee();

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectDao().create(project);
        databaseConnector.getSprintDao().create(sprint);

        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);
        databaseConnector.getTaskDao().addEmployee(task.getId(), employee);
        databaseConnector.getEmployeeDao().confirmTask(employee.getId(), task);

        TaskConfirmation taskConfirmation = databaseConnector.getTaskConfirmationDao().findByTaskIdAndEmployeeId(task.getId(), employee.getId());

        assertEquals(TaskConfirmation.Status.CONFIRMED, taskConfirmation.getStatus());

    }

    private void assertWeakEquals(Employee expected, Employee actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getLogin(), actual.getLogin());
        assertEquals(expected.getPosition(), actual.getPosition());
        assertTrue(TestUtils.equalContentCollections(expected.getRoles(), actual.getRoles()));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTaskConfirmations(),
                actual.getTaskConfirmations(),
                (c1, c2) -> c1.getStatus().equals(c2.getStatus())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTasks(),
                actual.getTasks(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
    }

    private boolean weakEquals(Employee expected, Employee actual) {
        return expected.getId().equals(actual.getId())
                && expected.getFirstName().equals(actual.getFirstName())
                && expected.getLastName().equals(actual.getLastName())
                && expected.getLogin().equals(actual.getLogin())
                && expected.getPassword().equals(actual.getPassword())
                && expected.getPosition().equals(actual.getPosition())
                && TestUtils.equalContentCollections(expected.getRoles(), actual.getRoles())
                && TestUtils.equalContentCollections(
                        expected.getTaskConfirmations(),
                        actual.getTaskConfirmations(),
                        (c1, c2) -> c1.getStatus().equals(c2.getStatus()))
                && TestUtils.equalContentCollections(
                        expected.getTasks(),
                        actual.getTasks(),
                        (t1, t2) -> t1.getId().equals(t2.getId()));
    }

}