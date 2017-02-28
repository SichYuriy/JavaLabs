package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper.SprintMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.SprintProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 09.02.2017.
 */
public class MyDatabaseSprintDao implements SprintDao {

    private static final String SPRINT_TABLE_NAME = "Sprint";
    private static final String TASK_TABLE_NAME = "Task";

    private SprintMapper sprintMapper = new SprintMapper();

    private Database database;

    private ProjectDao projectDao;
    private TaskDao taskDao;

    public MyDatabaseSprintDao(Database database) {
        this.database = database;
    }

    @Override
    public void create(Sprint sprint) {

        List<Pair<String, Object>> values = mapValues(sprint);

        if (sprint.getNextSprint() != null) {
            values.add(new Pair<>("nextSprintId", sprint.getNextSprint().getId()));
        }
        if (sprint.getPreviousSprint() != null) {
            values.add(new Pair<>("previousSprintId", sprint.getPreviousSprint().getId()));
        }
        values.add(new Pair<>("projectId", sprint.getProject().getId()));

        Long generatedId = database.insertInto(SPRINT_TABLE_NAME, values);

        sprint.setId(generatedId);
    }

    @Override
    public Sprint findById(Long id) {
        Record sprintRecord = database.selectFrom(SPRINT_TABLE_NAME, id);
        return (sprintRecord != null) ? parseRecord(sprintRecord) : null;
    }

    @Override
    public List<Sprint> findAll() {
        List<Record> sprintRecords = database.selectFrom(SPRINT_TABLE_NAME);

        return sprintRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Sprint sprint) {
        List<Pair<String, Object>> values = mapValues(sprint);
        database.update(SPRINT_TABLE_NAME, sprint.getId(), values);
    }

    @Override
    public void delete(Long id) {
        database.deleteFrom(SPRINT_TABLE_NAME, id);
    }

    @Override
    public List<Sprint> findByProjectId(Long id) {
        List<Record> sprintRecords = database.selectFrom(SPRINT_TABLE_NAME,
                "projectId", id);
        return sprintRecords.stream()
                .map(this::parseRecord)
                .collect(Collectors.toList());
    }

    @Override
    public Sprint findByTaskId(Long id) {
        Record task = database.selectFrom(TASK_TABLE_NAME, id);
        return (task != null) ? findById(task.getLong("id")) : null;
    }

    @Override
    public Sprint findByNextSprintId(Long id) {
        List<Record> sprintRecords = database.selectFrom(SPRINT_TABLE_NAME,
                "nextSprintId", id);
        if (sprintRecords.size() == 0) {
            return null;
        }
        return parseRecord(sprintRecords.get(0));
    }

    @Override
    public Sprint findByPreviousSprintId(Long id) {
        List<Record> sprintRecords = database.selectFrom(SPRINT_TABLE_NAME,
                "previousSprintId", id);
        if (sprintRecords.size() == 0) {
            return null;
        }
        return parseRecord(sprintRecords.get(0));
    }

    @Override
    public void updateNextSprint(Long sprintId, Long nextSprintId) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("nextSprintId", nextSprintId));
        database.update(SPRINT_TABLE_NAME, sprintId, values);
    }

    @Override
    public void deleteNextSprint(Long sprintId) {
        updateNextSprint(sprintId, null);
    }

    @Override
    public void updatePreviousSprint(Long sprintId, Long previousSprintId) {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("previousSprintId", previousSprintId));
        database.update(SPRINT_TABLE_NAME, sprintId, values);
    }

    @Override
    public void deletePreviousSprint(Long sprintId) {
        updatePreviousSprint(sprintId, null);
    }

    private List<Pair<String, Object>> mapValues(Sprint sprint) {
        List<Pair<String, Object>> values = new ArrayList<>();

        values.add(new Pair<>("name", sprint.getName()));
        values.add(new Pair<>("startDate", sprint.getStartDate()));
        values.add(new Pair<>("endDate", sprint.getEndDate()));
        values.add(new Pair<>("plannedStartDate", sprint.getPlannedStartDate()));
        values.add(new Pair<>("plannedEndDate", sprint.getPlannedEndDate()));
        values.add(new Pair<>("finished", sprint.getFinished()));
        return values;
    }

    private Sprint parseRecord(Record record) {
        SprintProxy sprintProxy = new SprintProxy(projectDao, taskDao, this);
        sprintMapper.map(sprintProxy, record);
        setProxies(sprintProxy, record);
        return sprintProxy;
    }

    private void setProxies(SprintProxy proxy, Record record) {
        proxy.setProjectId(record.getLong("projectId"));
        proxy.setNextSprintId(record.getLong("nextSprintId"));
        proxy.setPreviousSprintId(record.getLong("previousSprintId"));
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
}
