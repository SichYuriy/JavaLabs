package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.FakeData;
import com.gmail.at.sichyuriyy.netcracker.lab03.RelationUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 27.02.2017.
 */
public abstract class SprintDaoTest {

    private DatabaseConnector databaseConnector;
    private SprintDao sprintDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        sprintDao = databaseConnector.getSprintDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }


    @Test
    public void create() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint = TestData.getSprint(project);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint);

        Sprint dbSprint = sprintDao.findById(sprint.getId());

        assertWeakEquals(sprint, dbSprint);
    }

    @Test
    public void delete() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint = TestData.getSprint(project);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint);
        assertNotNull(sprintDao.findById(sprint.getId()));

        sprintDao.delete(sprint.getId());
        assertNull(sprintDao.findById(sprint.getId()));
    }

    @Test
    public void findAll() {
        Project project1 = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Project project2 = TestData.getProject(FakeData.getCustomer(2L), FakeData.getProjectManager(2L));

        Sprint sprint1 = TestData.getSprint(project1);
        Sprint sprint2 = TestData.getSprint(project1);
        Sprint sprint3 = TestData.getSprint(project2);

        databaseConnector.getProjectDao().create(project1);
        databaseConnector.getProjectDao().create(project2);
        sprintDao.create(sprint1);
        sprintDao.create(sprint2);
        sprintDao.create(sprint3);

        List<Sprint> expected = new ArrayList<>();
        expected.add(sprint1);
        expected.add(sprint2);
        expected.add(sprint3);

        List<Sprint> actual = sprintDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void update() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint = TestData.getSprint(project);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint);

        sprint.setFinished(false);
        sprint.setName("updatedName");
        sprint.setStartDate(new Date(10));
        sprint.setEndDate(new Date(101));
        sprint.setPlannedStartDate(new Date(10));
        sprint.setPlannedEndDate(new Date(101));
        sprintDao.update(sprint);

        Sprint dbSprint = sprintDao.findById(sprint.getId());

        assertWeakEquals(sprint, dbSprint);
    }

    @Test
    public void findByProjectId() {
        Project project1 = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Project project2 = TestData.getProject(FakeData.getCustomer(2L), FakeData.getProjectManager(2L));

        Sprint sprint1 = TestData.getSprint(project1);
        Sprint sprint2 = TestData.getSprint(project1);
        Sprint sprint3 = TestData.getSprint(project2);

        databaseConnector.getProjectDao().create(project1);
        databaseConnector.getProjectDao().create(project2);
        sprintDao.create(sprint1);
        sprintDao.create(sprint2);
        sprintDao.create(sprint3);

        List<Sprint> expected = new ArrayList<>();
        expected.add(sprint1);
        expected.add(sprint2);

        List<Sprint> actual = sprintDao.findByProjectId(project1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByTaskId() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint = TestData.getSprint(project);
        Task task = TestData.getTask(sprint);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint);
        databaseConnector.getTaskDao().create(task);

        RelationUtils.addTasks(sprint, task);

        Sprint dbSprint = sprintDao.findByTaskId(task.getId());

        assertWeakEquals(sprint, dbSprint);
    }

    @Test
    public void findByNextSprintId() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint1 = TestData.getSprint("sprint1", project);
        Sprint sprint2 = TestData.getSprint("sprint2", project);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint1);
        sprint2.setPreviousSprint(sprint1);
        sprintDao.create(sprint2);
        sprint1.setNextSprint(sprint2);
        sprintDao.updateNextSprint(sprint1.getId(), sprint2.getId());

        Sprint dbSprint = sprintDao.findByNextSprintId(sprint2.getId());

        assertWeakEquals(sprint1, dbSprint);
    }

    @Test
    public void findByPreviousSprintId() {
        Project project = TestData.getProject(FakeData.getCustomer(1L), FakeData.getProjectManager(1L));
        Sprint sprint1 = TestData.getSprint("sprint1", project);
        Sprint sprint2 = TestData.getSprint("sprint2", project);

        databaseConnector.getProjectDao().create(project);
        sprintDao.create(sprint1);
        sprint2.setPreviousSprint(sprint1);
        sprintDao.create(sprint2);
        sprint1.setNextSprint(sprint2);
        sprintDao.updateNextSprint(sprint1.getId(), sprint2.getId());

        Sprint dbSprint = sprintDao.findByPreviousSprintId(sprint1.getId());

        assertWeakEquals(sprint2, dbSprint);
    }

    private void assertWeakEquals(Sprint expected, Sprint actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getProject().getId(), actual.getProject().getId());
        assertEquals(expected.getStartDate(), actual.getStartDate());
        assertEquals(expected.getEndDate(), actual.getEndDate());
        assertEquals(expected.getPlannedStartDate(), actual.getPlannedStartDate());
        assertEquals(expected.getPlannedEndDate(), actual.getPlannedEndDate());
        assertEquals(expected.getFinished(), actual.getFinished());
        assertTrue(TestUtils.equals(expected.getPreviousSprint(), actual.getPreviousSprint(),
                (s1, s2) -> s1.getId().equals(s2.getId())));
        assertTrue(TestUtils.equals(expected.getNextSprint(), actual.getNextSprint(),
                (s1, s2) -> s1.getId().equals(s2.getId())));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTasks(),
                actual.getTasks(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
    }

    private boolean weakEquals(Sprint expected, Sprint actual) {
        return expected.getId().equals(actual.getId())
                && expected.getName().equals(actual.getName())
                && expected.getProject().getId().equals(actual.getProject().getId())
                && expected.getFinished().equals(actual.getFinished())
                && Objects.equals(expected.getStartDate(), actual.getStartDate())
                && Objects.equals(expected.getEndDate(), actual.getEndDate())
                && Objects.equals(expected.getPlannedStartDate(), actual.getPlannedStartDate())
                && Objects.equals(expected.getPlannedEndDate(), actual.getPlannedEndDate())
                && TestUtils.equals(expected.getNextSprint(), actual.getNextSprint(),
                        (s1, s2) -> s1.getId().equals(s2.getId()))
                && TestUtils.equals(expected.getPreviousSprint(), actual.getPreviousSprint(),
                        (s1, s2) -> s1.getId().equals(s2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getTasks(), actual.getTasks(),
                        (t1, t2) -> t1.getId().equals(t2.getId()));
    }

}