package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper.ProjectManagerMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 06.02.2017.
 */
public class MyDatabaseProjectManagerDao implements ProjectManagerDao {
    
    private static final String USER_TABLE_NAME = "User";
    private static final String EMPLOYEE_TABLE_NAME = "Employee";
    private static final String PROJECT_TABLE_NAME = "Project";
    private static final String USER_ROLE_TABLE_NAME = "user_role";
    private static final String ROLE_TABLE_NAME = "Role";
    private static final String TIME_REQUEST_TABLE_NAME = "TimeRequest";

    private ProjectManagerMapper managerMapper = new ProjectManagerMapper();

    private Database database;

    private TaskConfirmationDao taskConfirmationDao;
    private TaskDao taskDao;
    private TimeRequestDao timeRequestDao;
    private ProjectDao projectDao;
    private EmployeeDao employeeDao;
    private UserDao userDao;


    @Override
    public void create(ProjectManager manager) {

        List<Pair<String, Object>> userValues = new ArrayList<>();

        List<Long> roleIdList = manager.getRoles().stream()
                .map((r) -> (long) r.ordinal())
                .collect(Collectors.toList());

        userValues.add(new Pair<>("firstName", manager.getFirstName()));
        userValues.add(new Pair<>("lastName", manager.getLastName()));
        userValues.add(new Pair<>("login", manager.getLogin()));
        userValues.add(new Pair<>("password", manager.getPassword()));
        userValues.add(new Pair<>("roles", roleIdList));

        Long generatedUserId = database.insertInto(USER_TABLE_NAME, userValues);

        List<Pair<String, Object>> employeeValues = new ArrayList<>();
        employeeValues.add(new Pair<>("position", manager.getPosition().toString()));
        employeeValues.add(new Pair<>("userId_extend", generatedUserId));

        database.insertInto(EMPLOYEE_TABLE_NAME, employeeValues);

        manager.setId(generatedUserId);

    }

    @Override
    public ProjectManager findById(Long id) {
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
    public List<ProjectManager> findAll() {
        Long roleId = findRoleIdByName(Role.PROJECT_MANAGER.toString());
        List<Record> managerIdRecords = database.selectFrom(USER_ROLE_TABLE_NAME,
                "roleId", roleId);
        return managerIdRecords.stream()
                .map((record) -> record.getLong("userId"))
                .map(this::findById)
                .collect(Collectors.toList());


    }

    @Override
    public void update(ProjectManager manager) {
        employeeDao.update(manager);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(USER_TABLE_NAME, id);
        database.deleteFrom(EMPLOYEE_TABLE_NAME,
                "userId_extend", id);
    }

    @Override
    public ProjectManager findByProjectId(Long id) {
        Record projectRecord = database.selectFrom(PROJECT_TABLE_NAME, id);
        if (projectRecord == null) {
            return null;
        }
        Long projectManagerId = projectRecord.getLong("projectManagerId");

        return (projectManagerId != null) ? findById(projectManagerId) : null;
    }

    @Override
    public ProjectManager findByTimeRequestId(Long id) {
        Record requestRecord = database.selectFrom(TIME_REQUEST_TABLE_NAME, id);
        if (requestRecord == null) {
            return null;
        }
        return findById(requestRecord.getLong("projectManagerId"));
    }

    private ProjectManager parseRecord(Record record) {
        ProjectManagerProxy manager = new ProjectManagerProxy(projectDao, taskDao,
                timeRequestDao, taskConfirmationDao, userDao);
        managerMapper.map(manager, record);
        return manager;
    }

    private Long findRoleIdByName(String name) {
        List<Record> roleRecords = database.selectFrom(ROLE_TABLE_NAME,
                "name", name);
        if (roleRecords.size() == 0) {
            return null;
        }
        return roleRecords.get(0).getLong("id");
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

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
