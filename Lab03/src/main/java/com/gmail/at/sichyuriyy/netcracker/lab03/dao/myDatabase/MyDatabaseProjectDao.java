package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper.ProjectMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.ProjectProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 07.02.2017.
 */
public class MyDatabaseProjectDao implements ProjectDao {

    private static final String PROJECT_TABLE_NAME = "Project";
    private static final String SPRINT_TABLE_NAME = "Sprint";

    private ProjectMapper projectMapper = new ProjectMapper();

    private Database database;

    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;
    private SprintDao sprintDao;

    @Override
    public void create(Project project) {
        List<Pair<String, Object>> values = mapValues(project);

        if (!(project.getProjectManager() == null)) {
            values.add(new Pair<>("projectManagerId", project.getProjectManager().getId()));
        }
        values.add(new Pair<>("customerId", project.getCustomer().getId()));

        Long generatedId = database.insertInto(PROJECT_TABLE_NAME, values);

        project.setId(generatedId);
    }

    @Override
    public Project findById(Long id) {
        Record projectRecord = database.selectFrom(PROJECT_TABLE_NAME, id);

        return (projectRecord != null) ? parseRecord(projectRecord) : null;
    }

    @Override
    public List<Project> findAll() {
        List<Record> projectRecords = database.selectFrom(PROJECT_TABLE_NAME);

        return projectRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Project project) {
        List<Pair<String, Object>> values = mapValues(project);
        database.update(PROJECT_TABLE_NAME, project.getId(), values);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(PROJECT_TABLE_NAME, id);
    }

    @Override
    public List<Project> findByCustomerId(Long id) {
        List<Record> projectRecords = database.selectFrom(PROJECT_TABLE_NAME,
                "customerId", id);
        return projectRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByManagerId(Long id) {
        List<Record> projectRecords = database.selectFrom(PROJECT_TABLE_NAME,
                "managerId", id);
        return projectRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public Project findBySprintId(Long id) {
        Record sprintRecord = database.selectFrom(SPRINT_TABLE_NAME, id);
        if (sprintRecord == null) {
            return null;
        }
        return findById(sprintRecord.getLong("projectId"));
    }

    @Override
    public void updateProjectManager(Long projectId, Long managerId) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("managerId", managerId));
        database.update(PROJECT_TABLE_NAME, projectId, values);
    }

    @Override
    public void deleteProjectManager(Long projectId) {
        updateProjectManager(projectId, null);
    }

    @Override
    public void updateCustomer(Long projectId, Long customerId) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("customerId", customerId));
        database.update(PROJECT_TABLE_NAME, projectId, values);
    }

    @Override
    public void deleteCustomer(Long projectId) {
        updateCustomer(projectId, null);
    }

    private Project parseRecord(Record record) {
        ProjectProxy projectProxy = new ProjectProxy(projectManagerDao,
                customerDao, sprintDao);
        projectMapper.map(projectProxy, record);
        setProxies(projectProxy, record);
        return null;
    }

    private void setProxies(ProjectProxy proxy, Record record) {
        proxy.setCustomerId(record.getLong("customerId"));
        proxy.setManagerId(record.getLong("managerId"));
    }

    private List<Pair<String, Object>> mapValues(Project project) {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>("name", project.getName()));
        values.add(new Pair<>("startDate", project.getStartDate()));
        values.add(new Pair<>("endDate", project.getEndDate()));
        values.add(new Pair<>("plannedStartDate", project.getPlanedStartDate()));
        values.add(new Pair<>("plannedEndDate", project.getPlanedEndDate()));

        return values;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public ProjectManagerDao getProjectManagerDao() {
        return projectManagerDao;
    }

    public void setProjectManagerDao(ProjectManagerDao projectManagerDao) {
        this.projectManagerDao = projectManagerDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public SprintDao getSprintDao() {
        return sprintDao;
    }

    public void setSprintDao(SprintDao sprintDao) {
        this.sprintDao = sprintDao;
    }
}
