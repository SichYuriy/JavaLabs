package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskConfirmationDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.TaskConfirmationMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TaskConfirmationProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class MyDatabaseTaskConfirmationDao implements TaskConfirmationDao {

    private static final String TASK_CONFIRMATION_TABLE_NAME = "TaskConfirmation";

    private TaskConfirmationMapper confirmationMapper = new TaskConfirmationMapper();

    private Database database;

    private EmployeeDao employeeDao;
    private TaskDao taskDao;

    public MyDatabaseTaskConfirmationDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(TaskConfirmation confirmation) {
        List<Pair<String, Object>> values = mapValues(confirmation);
        values.add(new Pair<>("employeeId", confirmation.getEmployee().getId()));
        values.add(new Pair<>("taskId", confirmation.getTask().getId()));

        Long generatedId = database.insertInto(TASK_CONFIRMATION_TABLE_NAME, values);
        confirmation.setId(generatedId);
    }

    @Override
    public TaskConfirmation findById(Long id) {
        Record confirmationRecord = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME, id);
        return (confirmationRecord != null) ? parseRecord(confirmationRecord) : null;
    }

    @Override
    public List<TaskConfirmation> findAll() {
        List<Record> confirmationRecords = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME);
        return confirmationRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(TaskConfirmation confirmation) {
        List<Pair<String, Object>> values = mapValues(confirmation);
        database.update(TASK_CONFIRMATION_TABLE_NAME, confirmation.getId(), values);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(TASK_CONFIRMATION_TABLE_NAME, id);
    }

    @Override
    public List<TaskConfirmation> findByTaskId(Long id) {
        List<Record> confirmationRecords = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME,
                "taskId", id);
        return confirmationRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskConfirmation> findByEmployeeId(Long id) {
        List<Record> confirmationRecords = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME,
                "employeeId", id);
        return confirmationRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public TaskConfirmation findByTaskIdAndEmployeeId(Long taskId, Long employeeId) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("taskId", taskId));
        filters.add(new Pair<>("employeeId", employeeId));

        List<Record> confirmationRecords = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME,
                filters);
        if (confirmationRecords.size() == 0) {
            return null;
        }
        return parseRecord(confirmationRecords.get(0));
    }

    private List<Pair<String, Object>> mapValues(TaskConfirmation confirmation) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("status", confirmation.getStatus().toString()));

        return values;
    }

    private TaskConfirmation parseRecord(Record record) {
        TaskConfirmationProxy proxy = new TaskConfirmationProxy(employeeDao,
                taskDao);
        confirmationMapper.map(proxy, record);
        setProxies(proxy, record);
        return proxy;
    }

    private void setProxies(TaskConfirmationProxy proxy, Record record) {
        proxy.setEmployeeId(record.getLong("employeeId"));
        proxy.setTaskId(record.getLong("taskId"));
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

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
}
