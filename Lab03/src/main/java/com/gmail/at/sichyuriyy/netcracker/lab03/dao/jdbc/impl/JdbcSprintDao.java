package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.JdbcTemplate;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.SprintExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.PropertiesLoader;

import java.util.List;
import java.util.Properties;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcSprintDao extends AbstractJdbcDao implements SprintDao {

    private ProjectDao projectDao;
    private TaskDao taskDao;

    public JdbcSprintDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(Sprint sprint) {
        Transaction.tx(connectionManager, () -> {
            long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                    metamodelProperties.get(MetamodelProperties.SPRINT_TYPE));
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.NAME), sprint.getName());
            jdbcTemplate.insert(SqlRequest.INSERT_NUMBER_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.FINISHED), sprint.getFinished());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.START_DATE),
                    sprint.getStartDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.END_DATE),
                    sprint.getEndDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.PLANNED_START_DATE),
                    sprint.getPlannedStartDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.PLANNED_END_DATE),
                    sprint.getPlannedEndDate());
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.PROJECT_ID),
                    sprint.getProject().getId());
            if (sprint.getPreviousSprint() != null) {
                jdbcTemplate.insert(SqlRequest.INSERT_REF,
                        id, metamodelProperties.getProperty(MetamodelProperties.PREVIOUS_SPRINT_ID),
                        sprint.getPreviousSprint().getId());
            }
            if (sprint.getNextSprint() != null) {
                jdbcTemplate.insert(SqlRequest.INSERT_REF,
                        id, metamodelProperties.getProperty(MetamodelProperties.NEXT_SPRINT_ID),
                        sprint.getNextSprint().getId());
            }
            sprint.setId(id);
        });

    }

    @Override
    public Sprint findById(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_ID,
                new SprintExtractor(projectDao, taskDao, this),
                id, metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public List<Sprint> findAll() {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_ALL,
                new SprintExtractor(projectDao, taskDao, this),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public void update(Sprint sprint) {
        Transaction.tx(connectionManager, () -> {
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, sprint.getName(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.NAME));
            jdbcTemplate.update(SqlRequest.UPDATE_NUMBER_VALUE, sprint.getFinished(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.FINISHED));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, sprint.getStartDate(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.START_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, sprint.getEndDate(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.END_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, sprint.getPlannedStartDate(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.PLANNED_START_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, sprint.getPlannedEndDate(), sprint.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.PLANNED_END_DATE));
        });
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Sprint> findByProjectId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new SprintExtractor(projectDao, taskDao, this),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PROJECT_ID),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public Sprint findByTaskId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new SprintExtractor(projectDao, taskDao, this),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.SPRINT_ID),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public Sprint findByNextSprintId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new SprintExtractor(projectDao, taskDao, this),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PREVIOUS_SPRINT_ID),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public Sprint findByPreviousSprintId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new SprintExtractor(projectDao, taskDao, this),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.NEXT_SPRINT_ID),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE));
    }

    @Override
    public void updateNextSprint(Long sprintId, Long nextSprintId) {
        Transaction.tx(connectionManager, () -> {
           deleteNextSprint(sprintId);
           jdbcTemplate.insert(SqlRequest.INSERT_REF,
                   sprintId, metamodelProperties.getProperty(MetamodelProperties.NEXT_SPRINT_ID), nextSprintId);
        });
    }

    @Override
    public void deleteNextSprint(Long sprintId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE, sprintId,
                metamodelProperties.getProperty(MetamodelProperties.NEXT_SPRINT_ID));
    }

    @Override
    public void updatePreviousSprint(Long sprintId, Long previousSprintId) {
        Transaction.tx(connectionManager, () -> {
            deletePreviousSprint(sprintId);
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    sprintId, metamodelProperties.getProperty(MetamodelProperties.PREVIOUS_SPRINT_ID), previousSprintId);
        });
    }

    @Override
    public void deletePreviousSprint(Long sprintId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE, sprintId,
                metamodelProperties.getProperty(MetamodelProperties.PREVIOUS_SPRINT_ID));
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
