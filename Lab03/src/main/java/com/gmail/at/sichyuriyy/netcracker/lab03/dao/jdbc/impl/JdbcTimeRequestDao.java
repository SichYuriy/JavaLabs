package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TimeRequestDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.TimeRequestExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcTimeRequestDao extends AbstractJdbcDao implements TimeRequestDao {

    private ProjectManagerDao projectManagerDao;
    private TaskDao taskDao;

    public JdbcTimeRequestDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(TimeRequest request) {
        Transaction.tx(connectionManager, () -> {
           long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                   metamodelProperties.getProperty(MetamodelProperties.TIME_REQUEST_TYPE));
           jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                   id, metamodelProperties.getProperty(MetamodelProperties.REASON), request.getReason());
           jdbcTemplate.insert(SqlRequest.INSERT_NUMBER_VALUE,
                   id, metamodelProperties.getProperty(MetamodelProperties.REQUEST_TIME), request.getRequestTime());
            jdbcTemplate.insert(SqlRequest.INSERT_NUMBER_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.RESPONSE_TIME), request.getResponseTime());
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.STATUS), request.getStatus().toString());
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID), request.getTask().getId());
           request.setId(id);
        });
    }

    @Override
    public TimeRequest findById(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_ID,
                new TimeRequestExtractor(taskDao),
                id, metamodelProperties.getProperty(MetamodelProperties.TIME_REQUEST_TYPE));
    }

    @Override
    public List<TimeRequest> findAll() {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_ALL,
                new TimeRequestExtractor(taskDao),
                metamodelProperties.getProperty(MetamodelProperties.TIME_REQUEST_TYPE));
    }

    @Override
    public void update(TimeRequest request) {
        Transaction.tx(connectionManager, () -> {
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, request.getStatus().toString(), request.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.STATUS));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, request.getReason(), request.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.REASON));
            jdbcTemplate.update(SqlRequest.UPDATE_NUMBER_VALUE, request.getRequestTime(), request.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.REQUEST_TIME));
            jdbcTemplate.update(SqlRequest.UPDATE_NUMBER_VALUE, request.getResponseTime(), request.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.RESPONSE_TIME));
        });
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<TimeRequest> findByProjectId(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<TimeRequest> findByProjectManagerId(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<TimeRequest> findByTaskId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new TimeRequestExtractor(taskDao),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TIME_REQUEST_TYPE));
    }

    @Override
    public List<TimeRequest> findByEmployeeId(Long id) {
        throw new NotImplementedException();
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
