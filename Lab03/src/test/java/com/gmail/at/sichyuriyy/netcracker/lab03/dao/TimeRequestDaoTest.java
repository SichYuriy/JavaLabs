package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.FakeData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 27.02.2017.
 */
public abstract class TimeRequestDaoTest {

    private DatabaseConnector databaseConnector;
    private TimeRequestDao timeRequestDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        timeRequestDao = databaseConnector.getTimeRequestDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        TimeRequest timeRequest = TestData.getTimeRequest(task);

        databaseConnector.getTaskDao().create(task);
        timeRequestDao.create(timeRequest);

        TimeRequest dbTimeRequest = timeRequestDao.findById(timeRequest.getId());

        assertWeakEquals(timeRequest, dbTimeRequest);
    }

    @Test
    public void delete() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        TimeRequest timeRequest = TestData.getTimeRequest(task);

        timeRequestDao.create(timeRequest);
        assertNotNull(timeRequestDao.findById(timeRequest.getId()));
        timeRequestDao.delete(timeRequest.getId());
        assertNull(timeRequestDao.findById(timeRequest.getId()));
    }

    @Test
    public void findAll() {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(1L));

        TimeRequest timeRequest1 = TestData.getTimeRequest(task1);
        TimeRequest timeRequest2 = TestData.getTimeRequest(task2);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        timeRequestDao.create(timeRequest1);
        timeRequestDao.create(timeRequest2);

        List<TimeRequest> expected = new ArrayList<>();
        Collections.addAll(expected, timeRequest1, timeRequest2);

        List<TimeRequest> actual = timeRequestDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void update() {
        Task task = TestData.getTask(FakeData.getSprint(1L));
        TimeRequest timeRequest = TestData.getTimeRequest(task);

        databaseConnector.getTaskDao().create(task);
        timeRequestDao.create(timeRequest);
        timeRequest.setRequestTime(99);
        timeRequest.setResponseTime(98);
        timeRequest.setReason("updatedReason");
        timeRequest.setStatus(TimeRequest.Status.ACCEPTED);
        timeRequestDao.update(timeRequest);
        TimeRequest dbTimeRequest = timeRequestDao.findById(timeRequest.getId());

        assertWeakEquals(timeRequest, dbTimeRequest);
    }

    @Test
    public void findByProjectManagerId() {
        ProjectManager projectManager1 = TestData.getProjectManager("pm1");
        ProjectManager projectManager2 = TestData.getProjectManager("pm2");
        Project project1 = TestData.getProject("project1",FakeData.getCustomer(1L), projectManager1);
        Project project2 = TestData.getProject("project2",FakeData.getCustomer(1L), projectManager2);
        Sprint sprint1 = TestData.getSprint("pr1->sprint1", project1);
        Sprint sprint2 = TestData.getSprint("pr2->sprint1", project2);
        Task task1 = TestData.getTask("task1", sprint1);
        Task task2 = TestData.getTask("task2", sprint1);
        Task task3 = TestData.getTask("task3", sprint2);

        TimeRequest timeRequest1 = TestData.getTimeRequest(task1);
        TimeRequest timeRequest2 = TestData.getTimeRequest(task2);
        TimeRequest timeRequest3 = TestData.getTimeRequest(task3);

        databaseConnector.getProjectManagerDao().create(projectManager1);
        databaseConnector.getProjectManagerDao().create(projectManager2);
        databaseConnector.getProjectDao().create(project1);
        databaseConnector.getProjectDao().create(project2);
        databaseConnector.getSprintDao().create(sprint1);
        databaseConnector.getSprintDao().create(sprint2);
        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        databaseConnector.getTaskDao().create(task3);
        timeRequestDao.create(timeRequest1);
        timeRequestDao.create(timeRequest2);
        timeRequestDao.create(timeRequest3);

        List<TimeRequest> expected = new ArrayList<>();
        Collections.addAll(expected, timeRequest1, timeRequest2);

        List<TimeRequest> actual = timeRequestDao.findByProjectManagerId(projectManager1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByTaskId() {
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(1L));

        TimeRequest timeRequest1 = TestData.getTimeRequest(task1);
        TimeRequest timeRequest2 = TestData.getTimeRequest(task1);
        TimeRequest timeRequest3 = TestData.getTimeRequest(task2);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);
        timeRequestDao.create(timeRequest1);
        timeRequestDao.create(timeRequest2);
        timeRequestDao.create(timeRequest3);

        List<TimeRequest> expected = new ArrayList<>();
        Collections.addAll(expected, timeRequest1, timeRequest2);

        List<TimeRequest> actual = timeRequestDao.findByTaskId(task1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByEmployeeId() {
        Employee employee = TestData.getEmployee();

        ProjectManager projectManager = TestData.getProjectManager();
        Task task1 = TestData.getTask("task1", FakeData.getSprint(1L));
        Task task2 = TestData.getTask("task2", FakeData.getSprint(1L));

        TimeRequest timeRequest1 = TestData.getTimeRequest(task1);
        TimeRequest timeRequest2 = TestData.getTimeRequest(task2);

        databaseConnector.getEmployeeDao().create(employee);
        databaseConnector.getProjectManagerDao().create(projectManager);
        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);

        databaseConnector.getTaskDao().addEmployee(task1.getId(), employee);
        databaseConnector.getTaskDao().addEmployee(task2.getId(), employee);

        timeRequestDao.create(timeRequest1);
        timeRequestDao.create(timeRequest2);

        List<TimeRequest> expected = new ArrayList<>();
        Collections.addAll(expected, timeRequest1, timeRequest2);

        List<TimeRequest> actual = timeRequestDao.findByEmployeeId(employee.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    private void assertWeakEquals(TimeRequest expected, TimeRequest actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getReason(), actual.getReason());
        assertEquals(expected.getRequestTime(), actual.getRequestTime());
        assertEquals(expected.getResponseTime(), actual.getResponseTime());
        assertEquals(expected.getTask().getId(), actual.getTask().getId());
    }

    private boolean weakEquals(TimeRequest expected, TimeRequest actual) {
        return expected.getId().equals(actual.getId())
                && Objects.equals(expected.getStatus(), actual.getStatus())
                && Objects.equals(expected.getReason(), actual.getReason())
                && Objects.equals(expected.getRequestTime(), actual.getRequestTime())
                && Objects.equals(expected.getResponseTime(), actual.getResponseTime())
                && expected.getTask().getId().equals(actual.getTask().getId());
    }

}