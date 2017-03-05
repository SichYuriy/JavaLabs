package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.TaskMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TaskProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class MyDatabaseTaskDao implements TaskDao {

    private static final String TASK_TABLE_NAME = "Task";
    private static final String TIME_REQUEST_TABLE_NAME = "TimeRequest";
    private static final String TASK_CONFIRMATION_TABLE_NAME = "TaskConfirmation";
    private static final String TASK_EMPLOYEE_TABLE_NAME = "task_employee";
    private static final String TASK_DEPENDENCY_TABLE_NAME = "task_dependency";

    private TaskMapper taskMapper = new TaskMapper();

    private Database database;

    private EmployeeDao employeeDao;
    private TaskConfirmationDao taskConfirmationDao;
    private SprintDao sprintDao;
    private TimeRequestDao timeRequestDao;

    public MyDatabaseTaskDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(Task task) {
        List<Pair<String, Object>> values = mapValues(task);
        values.add(new Pair<>("sprintId", task.getSprint().getId()));
        if (task.getParentTask() != null) {
            values.add(new Pair<>("parentTaskId", task.getParentTask().getId()));
        }

        Long generatedId = database.insertInto(TASK_TABLE_NAME, values);

        task.setId(generatedId);
    }

    @Override
    public Task findById(Long id) {
        Record taskRecord = database.selectFrom(TASK_TABLE_NAME, id);
        return (taskRecord != null) ? parseRecord(taskRecord) : null;
    }

    @Override
    public List<Task> findAll() {
        List<Record> taskRecords = database.selectFrom(TASK_TABLE_NAME);
        return taskRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Task task) {
        List<Pair<String, Object>> values = mapValues(task);
        database.update(TASK_TABLE_NAME, task.getId(), values);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(TASK_TABLE_NAME, id);
    }

    @Override
    public List<Task> findByEmployeeId(Long id) {
        List<Record> taskIdRecords = database.selectFrom(TASK_EMPLOYEE_TABLE_NAME,
                "employeeId", id);
        return taskIdRecords.stream()
                .map((idRecord) -> idRecord.getLong("taskId"))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findBySprintId(Long id) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("parentTaskId", null));
        filters.add(new Pair<>("sprintId", id));
        List<Record> taskRecords = database.selectFrom(TASK_TABLE_NAME, filters);
        return taskRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findChildTasksByParentTaskId(Long id) {
        List<Record> taskRecords = database.selectFrom(TASK_TABLE_NAME,
                "parentTaskId", id);
        return taskRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findDependenciesByParentTaskId(Long id) {
        List<Record> dependencyIdRecords = database.selectFrom(TASK_DEPENDENCY_TABLE_NAME,
                "taskId", id);
        return dependencyIdRecords.stream()
                .map((idRecord) -> idRecord.getLong("dependencyId"))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public Task findByTimeRequestId(Long id) {
        Record requestRecord  = database.selectFrom(TIME_REQUEST_TABLE_NAME, id);
        if (requestRecord == null) {
            return null;
        }
        return findById(requestRecord.getLong("taskId"));
    }

    @Override
    public void updateEmployees(Long taskId, List<Employee> employees) {
        deleteAllEmployees(taskId);
        addEmployees(taskId, employees);
    }

    @Override
    public void addEmployee(Long taskId, Employee employee) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("taskId", taskId));
        values.add(new Pair<>("employeeId", employee.getId()));
        database.insertInto(TASK_EMPLOYEE_TABLE_NAME, values);
    }

    @Override
    public void addEmployees(Long taskId, List<Employee> employees) {
        employees.forEach((e) -> addEmployee(taskId, e));
    }

    @Override
    public void deleteEmployee(Long taskId, Employee employee) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("taskId", taskId));
        filters.add(new Pair<>("employeeId", employee.getId()));
        database.deleteFrom(TASK_EMPLOYEE_TABLE_NAME, filters);
    }

    @Override
    public void deleteAllEmployees(Long taskId) {
        database.deleteFrom(TASK_EMPLOYEE_TABLE_NAME,
                "taskId", taskId);
    }

    @Override
    public Task findByTaskConfirmationId(Long id) {
        Record confirmationRecord = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME, id);
        if (confirmationRecord == null) {
            return null;
        }
        return findById(confirmationRecord.getLong("taskId"));
    }

    @Override
    public Task findByChildTaskId(Long id) {
        Record childRecord = database.selectFrom(TASK_TABLE_NAME, id);
        if (childRecord == null) {
            return null;
        }
        Long parentTaskId = childRecord.getLong("parentTaskId");
        if (parentTaskId == null) {
            return null;
        }
        return findById(parentTaskId);
    }

    @Override
    public void updateDependencies(Long taskId, List<Task> dependencies) {
        deleteAllDependencies(taskId);
        addDependencies(taskId, dependencies);
    }

    @Override
    public void addDependency(Long taskId, Task dependency) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("taskId", taskId));
        values.add(new Pair<>("dependencyId", dependency.getId()));
        database.insertInto(TASK_DEPENDENCY_TABLE_NAME, values);
    }

    @Override
    public void addDependencies(Long taskId, List<Task> dependencies) {
        dependencies.forEach((d) -> addDependency(taskId, d));
    }

    @Override
    public void deleteDependency(Long taskId, Task dependency) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("taskId", taskId));
        filters.add(new Pair<>("dependencyId", dependency.getId()));
        database.deleteFrom(TASK_DEPENDENCY_TABLE_NAME, filters);
    }

    @Override
    public void deleteAllDependencies(Long taskId) {
        database.deleteFrom(TASK_DEPENDENCY_TABLE_NAME,
                "taskId", taskId);
    }

    private List<Pair<String, Object>> mapValues(Task task) {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>("name", task.getName()));
        values.add(new Pair<>("estimateTime", task.getEstimateTime()));
        values.add(new Pair<>("executionTime", task.getExecutionTime()));
        values.add(new Pair<>("status", task.getStatus().toString()));
        values.add(new Pair<>("requiredPosition", task.getRequiredPosition().toString()));
        return values;
    }

    private Task parseRecord(Record record) {
        TaskProxy proxy = new TaskProxy(employeeDao, this,
                taskConfirmationDao, sprintDao, timeRequestDao);
        taskMapper.map(proxy, record);
        setProxies(proxy, record);
        return proxy;
    }

    private void setProxies(TaskProxy proxy, Record record) {
        proxy.setSprintId(record.getLong("sprintId"));
        proxy.setParentTaskId(record.getLong("parentTaskId"));

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

    public TaskConfirmationDao getTaskConfirmationDao() {
        return taskConfirmationDao;
    }

    public void setTaskConfirmationDao(TaskConfirmationDao taskConfirmationDao) {
        this.taskConfirmationDao = taskConfirmationDao;
    }

    public SprintDao getSprintDao() {
        return sprintDao;
    }

    public void setSprintDao(SprintDao sprintDao) {
        this.sprintDao = sprintDao;
    }

    public TimeRequestDao getTimeRequestDao() {
        return timeRequestDao;
    }

    public void setTimeRequestDao(TimeRequestDao timeRequestDao) {
        this.timeRequestDao = timeRequestDao;
    }
}
