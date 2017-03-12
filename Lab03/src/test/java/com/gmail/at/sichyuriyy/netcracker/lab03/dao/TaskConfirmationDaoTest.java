package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.FakeData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 27.02.2017.
 */
public abstract class TaskConfirmationDaoTest {

    private DatabaseConnector databaseConnector;
    private TaskConfirmationDao taskConfirmationDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        taskConfirmationDao = databaseConnector.getTaskConfirmationDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        Employee employee = TestData.getEmployee();
        TaskConfirmation taskConfirmation = TestData.getTaskConfirmation();

        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);

        databaseConnector.getTaskDao().addEmployee(task.getId(), employee);

        TaskConfirmation dbTaskConfirmation = taskConfirmationDao.findByTaskIdAndEmployeeId(
                task.getId(),
                employee.getId()
        );

        assertWeakEquals(taskConfirmation, dbTaskConfirmation);
    }

    @Test
    public void delete() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        Employee employee = TestData.getEmployee();
        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);
        databaseConnector.getTaskDao().addEmployee(task.getId(), employee);

        databaseConnector.getTaskDao().deleteEmployee(task.getId(), employee);
        assertNull(taskConfirmationDao.findByTaskIdAndEmployeeId(task.getId(), employee.getId()));
    }

    @Test
    public void findByTaskId() {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(2L));
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation();
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation();

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        databaseConnector.getTaskDao().addEmployees(task1.getId(), Arrays.asList(employee1, employee2));
        databaseConnector.getTaskDao().addEmployee(task2.getId(), employee1);

        List<TaskConfirmation> expected = new ArrayList<>();
        expected.add(taskConfirmation1);
        expected.add(taskConfirmation2);

        List<TaskConfirmation> actual = taskConfirmationDao.findByTaskId(task1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByEmployeeId() throws Exception {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(2L));
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation();
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation();

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        databaseConnector.getTaskDao().addEmployees(task1.getId(), Arrays.asList(employee1, employee2));
        databaseConnector.getTaskDao().addEmployee(task2.getId(), employee1);

        List<TaskConfirmation> expected = new ArrayList<>();
        expected.add(taskConfirmation1);
        expected.add(taskConfirmation2);

        List<TaskConfirmation> actual = taskConfirmationDao.findByEmployeeId(employee1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByTaskIdAndEmployeeId() throws Exception {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(2L));
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation();

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        databaseConnector.getTaskDao().addEmployees(task1.getId(), Arrays.asList(employee1, employee2));
        databaseConnector.getTaskDao().addEmployee(task2.getId(), employee1);

        TaskConfirmation actual = taskConfirmationDao.findByTaskIdAndEmployeeId(
                task1.getId(), employee1.getId()
        );

        assertWeakEquals(taskConfirmation1, actual);
    }

    private void assertWeakEquals(TaskConfirmation expected, TaskConfirmation actual) {
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    private boolean weakEquals(TaskConfirmation expected, TaskConfirmation actual) {
        return expected.getStatus().equals(actual.getStatus());
    }

}