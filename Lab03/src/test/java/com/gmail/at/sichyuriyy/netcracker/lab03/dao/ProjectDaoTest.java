package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.RelationUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 26.02.2017.
 */
public abstract class ProjectDaoTest {

    private DatabaseConnector databaseConnector;
    private ProjectDao projectDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        projectDao = databaseConnector.getProjectDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);

        Project dbProject = projectDao.findById(project.getId());
        assertWeakEquals(dbProject, project);
    }

    @Test
    public void delete() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);

        projectDao.create(project);
        assertNotNull(projectDao.findById(project.getId()));

        projectDao.delete(project.getId());
        assertNull(projectDao.findById(project.getId()));
    }

    @Test
    public void findAll() {
        Customer customer1 = TestData.getCustomer("customer1");
        Customer customer2 = TestData.getCustomer("customer2");

        ProjectManager projectManager1 = TestData.getProjectManager("pm1");
        ProjectManager projectManager2 = TestData.getProjectManager("pm2");

        Project project1 = TestData.getProject("project1", customer1, projectManager1);
        Project project2 = TestData.getProject("project2", customer2, projectManager2);

        databaseConnector.getCustomerDao().create(customer1);
        databaseConnector.getCustomerDao().create(customer2);
        databaseConnector.getProjectManagerDao().create(projectManager1);
        databaseConnector.getProjectManagerDao().create(projectManager2);
        projectDao.create(project1);
        projectDao.create(project2);

        List<Project> expected = new ArrayList<>();
        expected.add(project1);
        expected.add(project2);

        List<Project> actual = projectDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void update() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);
        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);
        project.setName("updatedName");
        project.setStartDate(new Date(1));
        project.setEndDate(new Date(101));
        project.setPlannedStartDate(new Date(1));
        project.setPlannedEndDate(new Date(101));
        projectDao.update(project);

        Project dbProject = projectDao.findById(project.getId());

        assertWeakEquals(project, dbProject);
    }

    @Test
    public void findByCustomerId() {
        Customer customer1 = TestData.getCustomer("customer1");
        Customer customer2 = TestData.getCustomer("customer2");
        ProjectManager projectManager = TestData.getProjectManager();

        Project project1 = TestData.getProject("project1", customer1, projectManager);
        Project project2 = TestData.getProject("project2", customer1, projectManager);
        Project project3 = TestData.getProject("project3", customer2, projectManager);

        databaseConnector.getCustomerDao().create(customer1);
        databaseConnector.getCustomerDao().create(customer2);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project1);
        projectDao.create(project2);
        projectDao.create(project3);

        List<Project> expected = new ArrayList<>();
        expected.add(project1);
        expected.add(project2);

        List<Project> actual = projectDao.findByCustomerId(customer1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByManagerId() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager1 = TestData.getProjectManager("pm1");
        ProjectManager projectManager2 = TestData.getProjectManager("pm2");

        Project project1 = TestData.getProject("project1", customer, projectManager1);
        Project project2 = TestData.getProject("project2", customer, projectManager1);
        Project project3 = TestData.getProject("project3", customer, projectManager2);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager1);
        databaseConnector.getProjectManagerDao().create(projectManager2);
        projectDao.create(project1);
        projectDao.create(project2);
        projectDao.create(project3);

        List<Project> expected = new ArrayList<>();
        expected.add(project1);
        expected.add(project2);

        List<Project> actual = projectDao.findByManagerId(projectManager1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findBySprintId() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);
        Sprint sprint = TestData.getSprint(project);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);
        databaseConnector.getSprintDao().create(sprint);

        RelationUtils.addSprints(project, sprint);

        Project dbProject = projectDao.findBySprintId(sprint.getId());

        assertWeakEquals(project, dbProject);
    }

    @Test
    public void updateProjectManager() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager1 = TestData.getProjectManager("pm1");
        ProjectManager projectManager2 = TestData.getProjectManager("pm2");

        Project project = TestData.getProject(customer, projectManager1);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager1);
        databaseConnector.getProjectManagerDao().create(projectManager2);
        projectDao.create(project);
        projectDao.updateProjectManager(project.getId(), projectManager2.getId());

        project.setProjectManager(projectManager2);

        Project dbProject = projectDao.findById(project.getId());

        assertWeakEquals(project, dbProject);
    }

    @Test
    public void deleteProjectManager() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);
        projectDao.deleteProjectManager(project.getId());

        project.setProjectManager(null);

        Project dbProject = projectDao.findById(project.getId());

        assertWeakEquals(project, dbProject);
    }

    @Test
    public void updateCustomer() {
        Customer customer1 = TestData.getCustomer("customer1");
        Customer customer2 = TestData.getCustomer("customer2");
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer1, projectManager);

        databaseConnector.getCustomerDao().create(customer1);
        databaseConnector.getCustomerDao().create(customer2);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);
        projectDao.updateCustomer(project.getId(), customer2.getId());

        project.setCustomer(customer2);

        Project dbProject = projectDao.findById(project.getId());

        assertWeakEquals(project, dbProject);
    }

    @Test
    public void deleteCustomer() {
        Customer customer = TestData.getCustomer();
        ProjectManager projectManager = TestData.getProjectManager();

        Project project = TestData.getProject(customer, projectManager);

        databaseConnector.getCustomerDao().create(customer);
        databaseConnector.getProjectManagerDao().create(projectManager);
        projectDao.create(project);
        projectDao.deleteCustomer(project.getId());

        project.setCustomer(null);

        Project dbProject = projectDao.findById(project.getId());

        assertWeakEquals(project, dbProject);
    }

    private void assertWeakEquals(Project expected, Project actual) {
        assertEquals(expected.getId(), actual.getId());
        assertTrue(TestUtils.equals(expected.getCustomer(), actual.getCustomer(),
                (c1, c2) -> c1.getId().equals(c2.getId())));
        assertTrue(TestUtils.equals(expected.getProjectManager(), actual.getProjectManager(),
                (p1, p2) -> p1.getId().equals(p2.getId())));
        assertEquals(expected.getStartDate(), actual.getStartDate());
        assertEquals(expected.getEndDate(), actual.getEndDate());
        assertEquals(expected.getPlannedStartDate() ,actual.getPlannedStartDate());
        assertEquals(expected.getPlannedEndDate(), actual.getPlannedEndDate());
        assertEquals(expected.getName(), actual.getName());
        assertTrue(TestUtils.equalContentCollections(
                expected.getSprints(),
                actual.getSprints(),
                (s1, s2) -> s1.getId().equals(s2.getId())
        ));
    }

    private boolean weakEquals(Project expected, Project actual) {
        return expected.getId().equals(actual.getId())
                && TestUtils.equals(expected.getCustomer(), actual.getCustomer(),
                        (c1, c2) -> c1.getId().equals(c2.getId()))
                && TestUtils.equals(expected.getProjectManager(), actual.getProjectManager(),
                        (p1, p2) -> p1.getId().equals(p2.getId()))
                && Objects.equals(expected.getPlannedStartDate(), actual.getPlannedStartDate())
                && Objects.equals(expected.getPlannedEndDate(), actual.getPlannedEndDate())
                && Objects.equals(expected.getStartDate(), actual.getStartDate())
                && Objects.equals(expected.getEndDate(), actual.getEndDate())
                && expected.getName().equals(actual.getName())
                && TestUtils.equalContentCollections(
                        expected.getSprints(),
                        actual.getSprints(),
                        (s1, s2) -> s1.getId().equals(s2.getId()));
    }

}