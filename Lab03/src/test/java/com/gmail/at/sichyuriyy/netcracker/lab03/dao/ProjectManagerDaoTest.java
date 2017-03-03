package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.FakeData;
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
public abstract class ProjectManagerDaoTest {

    private DatabaseConnector databaseConnector;
    private ProjectManagerDao projectManagerDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        projectManagerDao = databaseConnector.getProjectManagerDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        ProjectManager projectManager = TestData.getProjectManager();
        projectManagerDao.create(projectManager);

        ProjectManager dbProjectManager = projectManagerDao.findById(projectManager.getId());
        assertWeakEquals(projectManager, dbProjectManager);
    }

    @Test
    public void delete() {
        ProjectManager projectManager = TestData.getProjectManager();
        projectManagerDao.create(projectManager);
        assertNotNull(projectManagerDao.findById(projectManager.getId()));

        projectManagerDao.delete(projectManager.getId());
        assertNull(projectManagerDao.findById(projectManager.getId()));
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

        List<ProjectManager> expectedList = new ArrayList<>();
        expectedList.add(projectManager);

        List<ProjectManager> actualList = projectManagerDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expectedList,
                actualList,
                this::weakEquals)
        );
    }

    @Test
    public void update() {
        ProjectManager projectManager = TestData.getProjectManager();
        projectManagerDao.create(projectManager);
        projectManager.setLogin("updatedLogin");
        projectManager.setPassword("updatedPassword");
        projectManager.setFirstName("updatedFirstName");
        projectManager.setLastName("updatedLastName");
        projectManager.setPosition(Employee.EmployeePosition.MIDDLE);
        projectManagerDao.update(projectManager);

        ProjectManager dbProjectManager = projectManagerDao.findById(projectManager.getId());
        assertWeakEquals(projectManager, dbProjectManager);
    }
    
    @Test
    public void findByProjectId() {
        ProjectManager projectManager  = TestData.getProjectManager();
        Customer customer = TestData.getCustomer();
        projectManagerDao.create(projectManager);

        Project project = TestData.getProject(customer, projectManager);
        databaseConnector.getProjectDao().create(project);

        RelationUtils.addManagedProject(projectManager, project);

        ProjectManager dbManager = projectManagerDao.findByProjectId(project.getId());

        assertWeakEquals(projectManager, dbManager);
    }

    @Test
    public void findByTimeRequestId() {
        ProjectManager projectManager = TestData.getProjectManager();
        Project project = TestData.getProject(FakeData.getCustomer(1L), projectManager);
        Sprint sprint = TestData.getSprint(project);
        Task task = TestData.getTask(sprint);

        TimeRequest timeRequest = TestData.getTimeRequest(task);
        projectManagerDao.create(projectManager);
        databaseConnector.getProjectDao().create(project);
        databaseConnector.getSprintDao().create(sprint);
        databaseConnector.getTaskDao().create(task);
        databaseConnector.getTimeRequestDao().create(timeRequest);

        RelationUtils.addManagedProject(projectManager, project);

        ProjectManager dbManager = projectManagerDao.findByTimeRequestId(timeRequest.getId());

        assertWeakEquals(projectManager, dbManager);
    }
    
    private void assertWeakEquals(ProjectManager expected, ProjectManager actual) {
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
                (c1, c2) -> c1.getId().equals(c2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTasks(),
                actual.getTasks(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getManagedProjects(),
                actual.getManagedProjects(),
                (p1, p2) -> p1.getId().equals(p2.getId())
        ));
    }

    private boolean weakEquals(ProjectManager expected, ProjectManager actual) {
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
                        (c1, c2) -> c1.getId().equals(c2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getTasks(),
                        actual.getTasks(),
                        (t1, t2) -> t1.getId().equals(t2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getManagedProjects(),
                        actual.getManagedProjects(),
                        (p1, p2) -> p1.getId().equals(p2.getId()));
    }

}