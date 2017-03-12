package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.EmployeeMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.EmployeeProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.sun.org.apache.regexp.internal.RE;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 31.01.2017.
 */
public class MyDatabaseEmployeeDao implements EmployeeDao {
    
    private static final String USER_TABLE_NAME = "User";
    private static final String EMPLOYEE_TABLE_NAME = "Employee";
    private static final String TASK_CONFIRMATION_TABLE_NAME = "TaskConfirmation";


    private EmployeeMapper employeeMapper = new EmployeeMapper();
    
    private Database database;

    private TaskConfirmationDao taskConfirmationDao;
    private TaskDao taskDao;
    private TimeRequestDao timeRequestDao;
    private UserDao userDao;

    public MyDatabaseEmployeeDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(Employee emp) {
        List<Pair<String, Object>> userValues = new ArrayList<>();

        userValues.add(new Pair<>("firstName", emp.getFirstName()));
        userValues.add(new Pair<>("lastName", emp.getLastName()));
        userValues.add(new Pair<>("login", emp.getLogin()));
        userValues.add(new Pair<>("password", emp.getPassword()));

        Long generatedUserId = database.insertInto(USER_TABLE_NAME, userValues);

        userDao.addRoles(generatedUserId, emp.getRoles());

        List<Pair<String, Object>> employeeValues = new ArrayList<>();
        employeeValues.add(new Pair<>("position", emp.getPosition().toString()));
        employeeValues.add(new Pair<>("userId_extend", generatedUserId));

        database.insertInto(EMPLOYEE_TABLE_NAME, employeeValues);

        emp.setId(generatedUserId);
    }

    @Override
    public Employee findById(Long id) {
        Record userRecord = database.selectFrom(USER_TABLE_NAME, id);
        List<Record> employeeRecords = database.selectFrom(EMPLOYEE_TABLE_NAME,
                "userId_extend", id);
        if (userRecord == null
                || employeeRecords.size() == 0) {
            return null;
        }
        Record employeeRecord = employeeRecords.get(0);
        Record join = userRecord.join(employeeRecord, "User", "Employee");
        return parseRecord(join);
    }

    @Override
    public List<Employee> findAll() {
        List<Record> employeeRecords = database.selectFrom(EMPLOYEE_TABLE_NAME);
        return employeeRecords.stream()
                .map((r) -> {
                    Long userRecordId = r.getLong("userId_extend");
                    Record userRecord = database.selectFrom(USER_TABLE_NAME, userRecordId);
                    if (userRecord == null) {
                        return null;
                    }
                    return userRecord.join(r, "User", "Employee");
                })
                .filter(Objects::nonNull)
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Employee emp) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("position", emp.getPosition().toString()));
        database.update(EMPLOYEE_TABLE_NAME, values,
                "userId_extend", emp.getId());
        userDao.update(emp);

    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(EMPLOYEE_TABLE_NAME,
                "userId_extend", id);
        database.deleteFrom(USER_TABLE_NAME, id);
    }

    @Override
    public List<Employee> findByTaskId(Long id) {
        List<Record> empIdRecords = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME,
                "taskId", id);

        return empIdRecords.stream()
                .map((record) -> record.getLong("employeeId"))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    @Override
    public void confirmTask(Long employeeId, Task task) {
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("employeeId", employeeId));
        filters.add(new Pair<>("taskId", task.getId()));

        List<Record> taskConfirmationRecord = database.selectFrom(TASK_CONFIRMATION_TABLE_NAME, filters);
        if (taskConfirmationRecord.isEmpty())
            return;
        Long taskConfirmationId = taskConfirmationRecord.get(0).getLong("id");
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("status", TaskConfirmation.Status.CONFIRMED.toString()));
        database.update(TASK_CONFIRMATION_TABLE_NAME, taskConfirmationId, values);
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public TaskConfirmationDao getTaskConfirmationDao() {
        return taskConfirmationDao;
    }

    public void setTaskConfirmationDao(TaskConfirmationDao taskConfirmationDao) {
        this.taskConfirmationDao = taskConfirmationDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public TimeRequestDao getTimeRequestDao() {
        return timeRequestDao;
    }

    public void setTimeRequestDao(TimeRequestDao timeRequestDao) {
        this.timeRequestDao = timeRequestDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private Employee parseRecord(Record record) {
        EmployeeProxy employee = new EmployeeProxy(taskDao, timeRequestDao,
                taskConfirmationDao, userDao);
        employeeMapper.map(employee, record);
        setProxies(employee, record);
        return employee;
    }

    private void setProxies(EmployeeProxy employee, Record record) {

    }


}
