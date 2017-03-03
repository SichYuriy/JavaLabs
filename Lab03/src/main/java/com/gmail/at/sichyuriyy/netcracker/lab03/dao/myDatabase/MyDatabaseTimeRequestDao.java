package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TimeRequestDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.TimeRequestMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TimeRequestProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class MyDatabaseTimeRequestDao implements TimeRequestDao {

    private static final String TIME_REQUEST_TABLE_NAME = "TimeRequest";
    private static final String TASK_EMPLOYEE_TABLE_NAME = "task_employee";
    private static final String SPRINT_TABLE_NAME = "Sprint";
    private static final String PROJECT_TABLE_NAME = "Project";
    private static final String TASK_TABLE_NAME = "Task";




    private TimeRequestMapper timeRequestMapper = new TimeRequestMapper();

    private Database database;

    private ProjectManagerDao projectManagerDao;
    private TaskDao taskDao;

    public MyDatabaseTimeRequestDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(TimeRequest request) {
        List<Pair<String, Object>> values = mapValues(request);
        values.add(new Pair<>("taskId", request.getTask().getId()));

        Long generatedId = database.insertInto(TIME_REQUEST_TABLE_NAME, values);

        request.setId(generatedId);
    }

    @Override
    public TimeRequest findById(Long id) {
        Record requestRecord = database.selectFrom(TIME_REQUEST_TABLE_NAME, id);
        return (requestRecord != null) ? parseRecord(requestRecord) : null;
    }

    @Override
    public List<TimeRequest> findAll() {
        List<Record> requestRecords = database.selectFrom(TIME_REQUEST_TABLE_NAME);
        return requestRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(TimeRequest request) {
        List<Pair<String, Object>> values = mapValues(request);
        database.update(TIME_REQUEST_TABLE_NAME, request.getId(), values);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(TIME_REQUEST_TABLE_NAME, id);
    }

    @Override
    public List<TimeRequest> findByProjectId(Long id) {
        List<Record> sprintRecords = database.selectFrom(SPRINT_TABLE_NAME,
                "projectId", id);

        return sprintRecords.stream()
                .map(sprintRecord -> database.selectFrom(
                                        TASK_TABLE_NAME,
                                        "sprintId",
                                        sprintRecord.getLong("id")))
                .flatMap(List::stream)
                .map(taskRecord -> taskRecord.getLong("id"))
                .map(this::findByTaskId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeRequest> findByProjectManagerId(Long id) {
        List<Record> projectRecords = database.selectFrom(PROJECT_TABLE_NAME,
                "managerId", id);
        return projectRecords.stream()
                .map(projectRecord -> projectRecord.getLong("id"))
                .map(this::findByProjectId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeRequest> findByTaskId(Long id) {
        List<Record> requestRecords = database.selectFrom(TIME_REQUEST_TABLE_NAME,
                "taskId", id);
        return requestRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeRequest> findByEmployeeId(Long id) {
        List<Record> taskIdRecords = database.selectFrom(TASK_EMPLOYEE_TABLE_NAME,
                "employeeId", id);
        return taskIdRecords.stream()
                .map(taskIdRecord -> taskIdRecord.getLong("taskId"))
                .map(this::findByTaskId)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Pair<String, Object>> mapValues(TimeRequest request) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("reason", request.getReason()));
        values.add(new Pair<>("requestTime", request.getRequestTime()));
        values.add(new Pair<>("responseTime", request.getResponseTime()));
        values.add(new Pair<>("status", request.getStatus().toString()));
        return values;
    }

    private TimeRequest parseRecord(Record record) {
        TimeRequestProxy proxy = new TimeRequestProxy(projectManagerDao, taskDao);
        timeRequestMapper.map(proxy, record);
        setProxies(proxy, record);
        return proxy;
    }

    private void setProxies(TimeRequestProxy proxy, Record record) {
        proxy.setManagerId(record.getLong("projectManagerId"));
        proxy.setTaskId(record.getLong("taskId"));
    }

    public ProjectManagerDao getProjectManagerDao() {
        return projectManagerDao;
    }

    public void setProjectManagerDao(ProjectManagerDao projectManagerDao) {
        this.projectManagerDao = projectManagerDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
}
