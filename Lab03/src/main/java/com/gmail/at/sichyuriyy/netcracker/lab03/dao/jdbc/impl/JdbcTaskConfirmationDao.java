package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskConfirmationDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.TaskConfirmationExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

import java.util.List;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcTaskConfirmationDao extends AbstractJdbcDao implements TaskConfirmationDao {

    private static final String SELECT_CONFIRMATION_BY_TASK_EMPLOYEE = SqlRequest.SELECT_BEGIN +
            "FROM refs conf_task_refs " +
            "INNER JOIN refs conf_emp_refs ON (conf_task_refs.entity_id=? AND conf_emp_refs.entity_id=?" +
                    " AND conf_task_refs.attribute_id=? AND conf_emp_refs.attribute_id=? " +
                    " AND conf_task_refs.owner_id=conf_emp_refs.owner_id) " +
            "INNER JOIN entity ON (conf_task_refs.owner_id=entity.entity_id) " +
            "INNER JOIN entity_type ON (entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
            SqlRequest.SELECT_END;

    private EmployeeDao employeeDao;
    private TaskDao taskDao;

    public JdbcTaskConfirmationDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public List<TaskConfirmation> findByTaskId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new TaskConfirmationExtractor(),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
    }

    @Override
    public List<TaskConfirmation> findByEmployeeId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new TaskConfirmationExtractor(),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
    }

    @Override
    public TaskConfirmation findByTaskIdAndEmployeeId(Long taskId, Long employeeId) {
        return jdbcTemplate.queryObject(SELECT_CONFIRMATION_BY_TASK_EMPLOYEE,
                new TaskConfirmationExtractor(),
                taskId, employeeId,
                metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
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
