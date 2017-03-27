package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.CustomerExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.EmployeeExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

import java.util.List;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcEmployeeDao extends AbstractJdbcDao implements EmployeeDao {

    private static final String SELECT_ALL_EMPLOYEES = SqlRequest.SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ON (filter_refs.ENTITY_ID=? AND filter_refs.OWNER_ID=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON ((entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
                                            "OR (entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_EMPLOYEE_BY_ID = SqlRequest.SELECT_BEGIN +
            "FROM entity " +
            "INNER JOIN entity_type " +
            "ON (entity.entity_id=? AND ((entity_type.entity_type_id=? AND entity.entity_type_id=entity_type.entity_type_id) " +
                                            "OR (entity_type.entity_type_id=?))) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_EMPLOYEE_BY_TASK_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs conf_task_refs " +
            "INNER JOIN refs conf_emp_refs ON (conf_task_refs.entity_id=? AND conf_task_refs.owner_id=conf_emp_refs.owner_id " +
                                            "AND conf_task_refs.attribute_id=? AND conf_emp_refs.attribute_id=?) " +
            "INNER JOIN entity ON (entity.entity_id=conf_emp_refs.entity_id) " +
            "INNER JOIN entity_type  ON ((entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
                                                "OR (entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;

    private static final String UPDATE_TASK_CONFIRMATION = "UPDATE value SET text_value=? WHERE attribute_id=? AND entity_id=( " +
            "SELECT conf_emp_refs.owner_id " +
            "FROM refs conf_emp_refs " +
            "INNER JOIN refs conf_task_refs ON (conf_emp_refs.entity_id=? AND conf_task_refs.entity_id=? " +
                                                "AND conf_emp_refs.attribute_id=? AND conf_task_refs.attribute_id=? " +
                                                "AND conf_emp_refs.owner_id=conf_task_refs.owner_id))";
    public JdbcEmployeeDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    private TaskConfirmationDao taskConfirmationDao;
    private TaskDao taskDao;
    private TimeRequestDao timeRequestDao;
    private JdbcUserDao userDao;

    @Override
    public void create(Employee employee) {
        Transaction.tx(connectionManager, () -> {
            userDao.createUser(employee, Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE)));
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    employee.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.POSITION),
                    employee.getPosition().toString());
        });
    }

    @Override
    public Employee findById(Long id) {
        return jdbcTemplate.queryObject(SELECT_EMPLOYEE_BY_ID,
                new EmployeeExtractor(taskDao, taskConfirmationDao, timeRequestDao, userDao),
                id, metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE)
        );
    }

    @Override
    public List<Employee> findAll() {
        return jdbcTemplate.queryObjects(SELECT_ALL_EMPLOYEES,
                new EmployeeExtractor(taskDao, taskConfirmationDao, timeRequestDao, userDao),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ROLE),
                metamodelProperties.getProperty(MetamodelProperties.ROLES),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    @Override
    public void update(Employee employee) {
        Transaction.tx(connectionManager, () -> {
           userDao.update(employee);
           jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, employee.getPosition().toString(),
                   employee.getId(),
                   metamodelProperties.getProperty(MetamodelProperties.POSITION));
        });
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Employee> findByTaskId(Long id) {

        return jdbcTemplate.queryObjects(SELECT_EMPLOYEE_BY_TASK_ID,
                new EmployeeExtractor(taskDao, taskConfirmationDao, timeRequestDao, userDao),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    @Override
    public void confirmTask(Long employeeId, Task task) {
        jdbcTemplate.update(UPDATE_TASK_CONFIRMATION,
                TaskConfirmation.Status.CONFIRMED.toString(),
                metamodelProperties.getProperty(MetamodelProperties.STATUS),
                employeeId,
                task.getId(),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_ID)
                );
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public TaskConfirmationDao getTaskConfirmationDao() {
        return taskConfirmationDao;
    }

    public void setTaskConfirmationDao(TaskConfirmationDao taskConfirmationDao) {
        this.taskConfirmationDao = taskConfirmationDao;
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

    public JdbcUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(JdbcUserDao userDao) {
        this.userDao = userDao;
    }
}
