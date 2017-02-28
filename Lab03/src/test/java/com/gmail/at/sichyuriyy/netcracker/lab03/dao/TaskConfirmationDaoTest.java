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
        TaskConfirmation taskConfirmation = TestData.getTaskConfirmation(employee, task);

        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);
        taskConfirmationDao.create(taskConfirmation);

        TaskConfirmation dbTaskConfirmation = taskConfirmationDao.findById(taskConfirmation.getId());

        assertWeakEquals(taskConfirmation, dbTaskConfirmation);
    }

    @Test
    public void delete() {
        TaskConfirmation taskConfirmation = TestData.getTaskConfirmation(
                FakeData.getEmployee(1L),
                FakeData.getTask(1L)
        );
        taskConfirmationDao.create(taskConfirmation);
        assertNotNull(taskConfirmationDao.findById(taskConfirmation.getId()));

        taskConfirmationDao.delete(taskConfirmation.getId());
        assertNull(taskConfirmationDao.findById(taskConfirmation.getId()));
    }

    @Test
    public void findAll() {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(2L));
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation(employee1, task1);
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation(employee2, task2);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        taskConfirmationDao.create(taskConfirmation1);
        taskConfirmationDao.create(taskConfirmation2);

        List<TaskConfirmation> expected = new ArrayList<>();
        expected.add(taskConfirmation1);
        expected.add(taskConfirmation2);

        List<TaskConfirmation> actual = taskConfirmationDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void update() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        Employee employee = TestData.getEmployee();
        TaskConfirmation taskConfirmation = TestData.getTaskConfirmation(employee, task);

        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);
        taskConfirmationDao.create(taskConfirmation);
        taskConfirmation.setStatus(TaskConfirmation.ConfirmationStatus.CONFIRMED);
        taskConfirmationDao.update(taskConfirmation);
        TaskConfirmation dbTaskConfirmation = taskConfirmationDao.findById(taskConfirmation.getId());

        assertWeakEquals(taskConfirmation, dbTaskConfirmation);
    }

    @Test
    public void findByTaskId() {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(2L));
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation(employee1, task1);
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation(employee2, task1);
        TaskConfirmation taskConfirmation3 = TestData.getTaskConfirmation(employee1, task2);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        taskConfirmationDao.create(taskConfirmation1);
        taskConfirmationDao.create(taskConfirmation2);
        taskConfirmationDao.create(taskConfirmation3);

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

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation(employee1, task1);
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation(employee1, task2);
        TaskConfirmation taskConfirmation3 = TestData.getTaskConfirmation(employee2, task1);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        taskConfirmationDao.create(taskConfirmation1);
        taskConfirmationDao.create(taskConfirmation2);
        taskConfirmationDao.create(taskConfirmation3);

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

        TaskConfirmation taskConfirmation1 = TestData.getTaskConfirmation(employee1, task1);
        TaskConfirmation taskConfirmation2 = TestData.getTaskConfirmation(employee1, task2);
        TaskConfirmation taskConfirmation3 = TestData.getTaskConfirmation(employee2, task1);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        taskConfirmationDao.create(taskConfirmation1);
        taskConfirmationDao.create(taskConfirmation2);
        taskConfirmationDao.create(taskConfirmation3);

        TaskConfirmation actual = taskConfirmationDao.findByTaskIdAndEmployeeId(
                task1.getId(), employee1.getId()
        );

        assertWeakEquals(taskConfirmation1, actual);
    }

    private void assertWeakEquals(TaskConfirmation expected, TaskConfirmation actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmployee().getId(), actual.getEmployee().getId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getTask().getId(), actual.getTask().getId());
    }

    private boolean weakEquals(TaskConfirmation expected, TaskConfirmation actual) {
        return expected.getId().equals(actual.getId())
                && expected.getStatus().equals(actual.getStatus())
                && expected.getEmployee().getId().equals(actual.getEmployee().getId())
                && expected.getTask().getId().equals(actual.getTask().getId());
    }

}