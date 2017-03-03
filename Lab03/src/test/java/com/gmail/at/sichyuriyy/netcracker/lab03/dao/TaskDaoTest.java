package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.FakeData;
import com.gmail.at.sichyuriyy.netcracker.lab03.RelationUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections.CollectionsDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 27.02.2017.
 */
public abstract class TaskDaoTest {

    private DatabaseConnector databaseConnector;
    private TaskDao taskDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        taskDao = databaseConnector.getTaskDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task);
        Task dbTask = taskDao.findById(task.getId());
        assertWeekEquals(task, dbTask);
    }

    @Test
    public void delete() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task);
        assertNotNull(taskDao.findById(task.getId()));
        taskDao.delete(task.getId());
        assertNull(taskDao.findById(task.getId()));
    }

    @Test
    public void findAll() {
        Sprint sprint1 = TestData.getSprint(FakeData.getProject(1L));
        Sprint sprint2 = TestData.getSprint(FakeData.getProject(2L));
        Task task1 = TestData.getTask(sprint1);
        Task task2 = TestData.getTask(sprint1);
        Task task3 = TestData.getTask(sprint2);
        databaseConnector.getSprintDao().create(sprint1);
        databaseConnector.getSprintDao().create(sprint2);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);

        List<Task> expected = new ArrayList<>();
        Collections.addAll(expected, task1, task2, task3);

        List<Task> actual = taskDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void update() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task);
        task.setName("updatedName");
        task.setEstimateTime(125);
        task.setExecutionTime(120);
        task.setStatus(Task.TaskStatus.WORKING);
        taskDao.update(task);
        Task dbTask = taskDao.findById(task.getId());
        assertWeekEquals(task, dbTask);
    }

    @Test
    public void findByEmployeeId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");

        databaseConnector.getSprintDao().create(sprint);
        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);

        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);

        taskDao.addEmployee(task1.getId(), employee1);
        taskDao.addEmployee(task1.getId(), employee2);
        taskDao.addEmployee(task2.getId(), employee1);

        RelationUtils.addTaskEmployeeRelation(task1, employee1, employee2);
        RelationUtils.addTaskEmployeeRelation(task2, employee1);

        List<Task> expected = new ArrayList<>();
        Collections.addAll(expected, task1, task2);

        List<Task> actual = taskDao.findByEmployeeId(employee1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findBySprintId() {
        Sprint sprint1 = TestData.getSprint(FakeData.getProject(1L));
        Sprint sprint2 = TestData.getSprint(FakeData.getProject(1L));

        Task task1 = TestData.getTask("task1", sprint1);
        Task task2 = TestData.getTask("task2", sprint1);
        Task task3 = TestData.getTask("task3", sprint2);

        databaseConnector.getSprintDao().create(sprint1);
        databaseConnector.getSprintDao().create(sprint2);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);

        List<Task> expected = new ArrayList<>();
        Collections.addAll(expected, task1, task2);

        List<Task> actual = taskDao.findBySprintId(sprint1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findChildTasksByParentTaskId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);
        Task task3 = TestData.getTask("task3", sprint);
        Task task4 = TestData.getTask("task4", sprint);

        task2.setParentTask(task1);
        task3.setParentTask(task1);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);
        taskDao.create(task4);

        List<Task> expected = new ArrayList<>();
        Collections.addAll(expected, task2, task3);

        List<Task> actual = taskDao.findChildTasksByParentTaskId(task1.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findDependenciesByParentTaskId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);
        Task task3 = TestData.getTask("task3", sprint);
        Task task4 = TestData.getTask("task4", sprint);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);
        taskDao.create(task4);

        taskDao.addDependency(task3.getId(), task1);
        taskDao.addDependency(task3.getId(), task2);

        RelationUtils.addDependencies(task3, task1, task2);

        List<Task> expected = new ArrayList<>();
        Collections.addAll(expected, task1, task2);

        List<Task> actual = taskDao.findDependenciesByParentTaskId(task3.getId());

        assertTrue(TestUtils.equalContentCollections(
                expected,
                actual,
                this::weakEquals
        ));
    }

    @Test
    public void findByTimeRequestId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        TimeRequest timeRequest = TestData.getTimeRequest(task);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task);
        databaseConnector.getTimeRequestDao().create(timeRequest);

        RelationUtils.addTimeRequests(task, timeRequest);

        Task actual = taskDao.findByTimeRequestId(timeRequest.getId());

        assertWeekEquals(task, actual);
    }

    @Test
    public void updateEmployees() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        Employee employee1 = TestData.getEmployee("emp1");
        Employee employee2 = TestData.getEmployee("emp2");
        Employee employee3 = TestData.getEmployee("emp3");

        databaseConnector.getSprintDao().create(sprint);
        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee1);
        databaseConnector.getEmployeeDao().create(employee2);
        databaseConnector.getEmployeeDao().create(employee3);

        taskDao.addEmployee(task.getId(), employee1);

        List<Employee> employeeList = new ArrayList<>();
        Collections.addAll(employeeList, employee2, employee3);
        taskDao.updateEmployees(task.getId(), employeeList);

        RelationUtils.addTaskEmployeeRelation(task, employee2, employee3);
        Task actual = taskDao.findById(task.getId());
        assertWeekEquals(task, actual);
    }

    @Test
    public void deleteEmployee() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        Employee employee = TestData.getEmployee();

        databaseConnector.getSprintDao().create(sprint);
        databaseConnector.getTaskDao().create(task);
        databaseConnector.getEmployeeDao().create(employee);

        taskDao.addEmployee(task.getId(), employee);
        taskDao.deleteEmployee(task.getId(), employee);

        Task actual = taskDao.findById(task.getId());
        assertWeekEquals(task, actual);
    }

    @Test
    public void findByTaskConfirmationId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task = TestData.getTask(sprint);
        TaskConfirmation taskConfirmation = TestData.getTaskConfirmation(FakeData.getEmployee(1L), task);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task);
        databaseConnector.getTaskConfirmationDao().create(taskConfirmation);

        RelationUtils.addTaskConfirmations(task, taskConfirmation);

        Task actual = taskDao.findByTaskConfirmationId(taskConfirmation.getId());

        assertWeekEquals(task, actual);
    }

    @Test
    public void findByChildTaskId() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task parent = TestData.getTask("parent", sprint);
        Task child = TestData.getTask("child", sprint);
        child.setParentTask(parent);
        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(parent);
        taskDao.create(child);

        RelationUtils.addChildTasksRelation(parent, child);

        Task actual = taskDao.findByChildTaskId(child.getId());

        assertWeekEquals(parent, actual);
    }

    @Test
    public void updateDependencies() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);
        Task task3 = TestData.getTask("task3", sprint);
        Task task4 = TestData.getTask("task4", sprint);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.create(task3);
        taskDao.create(task4);
        taskDao.addDependency(task4.getId(), task1);
        List<Task> dependencies = new ArrayList<>();
        Collections.addAll(dependencies, task2, task3);
        taskDao.updateDependencies(task4.getId(), dependencies);
        RelationUtils.addDependencies(task4, task2, task3);

        Task actual = taskDao.findById(task4.getId());

        assertWeekEquals(task4, actual);
    }

    @Test
    public void deleteDependency() {
        Sprint sprint = TestData.getSprint(FakeData.getProject(1L));
        Task task1 = TestData.getTask("task1", sprint);
        Task task2 = TestData.getTask("task2", sprint);

        databaseConnector.getSprintDao().create(sprint);
        taskDao.create(task1);
        taskDao.create(task2);
        taskDao.addDependency(task2.getId(), task1);
        taskDao.deleteDependency(task2.getId(), task1);

        Task actual = taskDao.findById(task2.getId());

        assertWeekEquals(task2, actual);
    }

    private void assertWeekEquals(Task expected, Task actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSprint().getId(), actual.getSprint().getId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getEstimateTime(), actual.getEstimateTime());
        assertEquals(expected.getExecutionTime(), actual.getExecutionTime());
        assertEquals(expected.getRequiredPosition(), actual.getRequiredPosition());
        assertTrue(TestUtils.equalContentCollections(
                expected.getChildTasks(),
                actual.getChildTasks(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getDependencies(),
                actual.getDependencies(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
        assertTrue(TestUtils.equals(
                expected.getParentTask(),
                actual.getParentTask(),
                (t1, t2) -> t1.getId().equals(t2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTaskConfirmations(),
                actual.getTaskConfirmations(),
                (c1, c2) -> c1.getId().equals(c2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getTimeRequests(),
                actual.getTimeRequests(),
                (r1, r2) -> r1.getId().equals(r2.getId())
        ));
        assertTrue(TestUtils.equalContentCollections(
                expected.getEmployees(),
                actual.getEmployees(),
                (e1, e2) -> e1.getId().equals(e2.getId())
        ));
    }

    private boolean weakEquals(Task expected, Task actual) {
        return expected.getId().equals(actual.getId())
                && expected.getName().equals(actual.getName())
                && expected.getSprint().getId().equals(actual.getSprint().getId())
                && Objects.equals(expected.getStatus(), actual.getStatus())
                && Objects.equals(expected.getEstimateTime(), actual.getEstimateTime())
                && Objects.equals(expected.getExecutionTime(), actual.getExecutionTime())
                && Objects.equals(expected.getRequiredPosition(), actual.getRequiredPosition())
                && TestUtils.equals(expected.getParentTask(), actual.getParentTask(),
                        (t1, t2) -> t1.getId().equals(t2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getChildTasks(),
                        actual.getChildTasks(),
                        (t1, t2) -> t1.getId().equals(t2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getDependencies(),
                        actual.getDependencies(),
                        (t1, t2) -> t1.getId().equals(t2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getTaskConfirmations(),
                        actual.getTaskConfirmations(),
                        (c1, c2) -> c1.getId().equals(c2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getTimeRequests(),
                        actual.getTimeRequests(),
                        (r1, r2) -> r1.getId().equals(r2.getId()))
                && TestUtils.equalContentCollections(
                        expected.getEmployees(),
                        actual.getEmployees(),
                        (e1, e2) -> e1.getId().equals(e2.getId()));
    }

}