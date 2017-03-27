package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.TaskExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcTaskDao extends AbstractJdbcDao implements TaskDao {

    private static final String SELECT_TASKS_BY_EMPLOYEE_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs conf_emp_refs " +
            "INNER JOIN entity emp_entity ON (emp_entity.entity_id=? AND emp_entity.entity_type_id=? AND conf_emp_refs.entity_id=emp_entity.entity_id " +
                                            "AND conf_emp_refs.attribute_id=?) " +
            "INNER JOIN refs conf_task_refs ON (conf_task_refs.owner_id=conf_emp_refs.owner_id AND conf_task_refs.attribute_id=?) " +
            "INNER JOIN entity ON (entity.entity_id=conf_task_refs.entity_id AND entity.entity_type_id=?) " +
            "INNER JOIN entity_type ON (entity_type.entity_type_id=entity.entity_type_id) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_TASK_CONFIRMATION_ID_BY_TASK_EMPLOYEE = "" +
            "SELECT entity.entity_id id " +
            "FROM refs conf_task_refs " +
            "INNER JOIN refs conf_emp_refs ON (conf_task_refs.entity_id=? AND conf_emp_refs.entity_id=?" +
                                                    " AND conf_task_refs.attribute_id=? AND conf_emp_refs.attribute_id=? " +
                                                    " AND conf_task_refs.owner_id=conf_emp_refs.owner_id) " +
            "INNER JOIN entity ON (conf_task_refs.owner_id=entity.entity_id AND entity.entity_type_id=?)";

    private static final String SELECT_TASK_CONFIRMATIONS_BY_TASK = "" +
            "SELECT entity.entity_id id " +
            "FROM refs conf_task_refs " +
            "INNER JOIN refs conf_emp_refs ON (conf_task_refs.entity_id=? " +
                                                " AND conf_task_refs.attribute_id=? AND conf_emp_refs.attribute_id=? " +
                                                " AND conf_task_refs.owner_id=conf_emp_refs.owner_id) " +
            "INNER JOIN entity ON (conf_task_refs.owner_id=entity.entity_id AND entity.entity_type_id=?)";

    private EmployeeDao employeeDao;
    private TaskConfirmationDao taskConfirmationDao;
    private SprintDao sprintDao;
    private TimeRequestDao timeRequestDao;

    public JdbcTaskDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(Task task) {
        Transaction.tx(connectionManager, ()-> {
            long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                    metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.NAME), task.getName());
            jdbcTemplate.insert(SqlRequest.INSERT_NUMBER_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.ESTIMATE_TIME), task.getEstimateTime());
            jdbcTemplate.insert(SqlRequest.INSERT_NUMBER_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.EXECUTION_TIME), task.getExecutionTime());
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.REQUIRED_POSITION), task.getRequiredPosition().toString());
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.STATUS), task.getStatus().toString());
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.SPRINT_ID), task.getSprint().getId());
            if (task.getParentTask() != null) {
                jdbcTemplate.insert(SqlRequest.INSERT_REF,
                        id, metamodelProperties.getProperty(MetamodelProperties.PARENT_TASK_ID), task.getParentTask().getId());
            }
            task.setId(id);
        });
    }

    @Override
    public Task findById(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public List<Task> findAll() {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_ALL,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public void update(Task task) {
        Transaction.tx(connectionManager, () -> {
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, task.getName(), task.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.NAME));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, task.getStatus().toString(), task.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.STATUS));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, task.getRequiredPosition().toString(), task.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.REQUIRED_POSITION));
            jdbcTemplate.update(SqlRequest.UPDATE_NUMBER_VALUE, task.getEstimateTime(), task.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.ESTIMATE_TIME));
            jdbcTemplate.update(SqlRequest.UPDATE_NUMBER_VALUE, task.getExecutionTime(), task.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.EXECUTION_TIME));
        });
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Task> findByEmployeeId(Long id) {
        return jdbcTemplate.queryObjects(SELECT_TASKS_BY_EMPLOYEE_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                id, metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public List<Task> findBySprintId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.SPRINT_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public List<Task> findChildTasksByParentTaskId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PARENT_TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public List<Task> findDependenciesByParentTaskId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.DEPENDENCIES),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public Task findByTimeRequestId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.TIME_REQUEST_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public void updateEmployees(Long taskId, List<Employee> employees) {
        Transaction.tx(connectionManager, () -> {
            deleteAllEmployees(taskId);
            addEmployees(taskId, employees);
        });
    }

    @Override
    public void addEmployee(Long taskId, Employee employee) {
        Transaction.tx(connectionManager, () -> {
            long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                    metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID), taskId);
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID), employee.getId());
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.STATUS),
                    TaskConfirmation.Status.UNCONFIRMED.toString());
        });
    }

    @Override
    public void addEmployees(Long taskId, List<Employee> employees) {
        Transaction.tx(connectionManager, () -> {
            for (Employee employee: employees) {
                addEmployee(taskId, employee);
            };
        });
    }

    @Override
    public void deleteEmployee(Long taskId, Employee employee) {
        Transaction.tx(connectionManager, () -> {
           Long taskConfirmationId = jdbcTemplate.queryObject(SELECT_TASK_CONFIRMATION_ID_BY_TASK_EMPLOYEE,
                   new ConfirmationIdExtractor(),
                   taskId, employee.getId(),
                   metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                   metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                   metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
            super.delete(taskConfirmationId);
        });
    }

    @Override
    public void deleteAllEmployees(Long taskId) {
        Transaction.tx(connectionManager, () -> {
            List<Long> taskConfirmationIdList = jdbcTemplate.queryObjects(SELECT_TASK_CONFIRMATIONS_BY_TASK,
                    new ConfirmationIdExtractor(),
                    taskId,
                    metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                    metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ID),
                    metamodelProperties.getProperty(MetamodelProperties.TASK_CONFIRMATION_TYPE));
            for (Long id: taskConfirmationIdList) {
                super.delete(id);
            }
        });
    }

    @Override
    public Task findByChildTaskId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new TaskExtractor(employeeDao, this, taskConfirmationDao, sprintDao, timeRequestDao),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PARENT_TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE));
    }

    @Override
    public void updateDependencies(Long taskId, List<Task> dependencies) {
        Transaction.tx(connectionManager, () -> {
            deleteAllDependencies(taskId);
            addDependencies(taskId, dependencies);
        });

    }

    @Override
    public void addDependency(Long taskId, Task dependency) {
        jdbcTemplate.insert(SqlRequest.INSERT_REF,
                taskId, metamodelProperties.getProperty(MetamodelProperties.DEPENDENCIES), dependency.getId());
    }

    @Override
    public void addDependencies(Long taskId, List<Task> dependencies) {
        Transaction.tx(connectionManager, () -> {
            for (Task dependency: dependencies) {
                addDependency(taskId, dependency);
            }
        });
    }

    @Override
    public void deleteDependency(Long taskId, Task dependency) {
        jdbcTemplate.update(SqlRequest.DELETE_REF,
                taskId, metamodelProperties.getProperty(MetamodelProperties.DEPENDENCIES), dependency.getId());
    }

    @Override
    public void deleteAllDependencies(Long taskId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE,
                taskId, metamodelProperties.getProperty(MetamodelProperties.DEPENDENCIES));
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public TaskConfirmationDao getTaskConfirmationDao() {
        return taskConfirmationDao;
    }

    public void setTaskConfirmationDao(TaskConfirmationDao taskConfirmationDao) {
        this.taskConfirmationDao = taskConfirmationDao;
    }

    public SprintDao getSprintDao() {
        return sprintDao;
    }

    public void setSprintDao(SprintDao sprintDao) {
        this.sprintDao = sprintDao;
    }

    public TimeRequestDao getTimeRequestDao() {
        return timeRequestDao;
    }

    public void setTimeRequestDao(TimeRequestDao timeRequestDao) {
        this.timeRequestDao = timeRequestDao;
    }

    private static class ConfirmationIdExtractor implements EntityExtractor<Long> {

        @Override
        public List<Long> extract(ResultSet rs) throws SQLException {
            List<Long> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getLong("id"));
            }
            return result;
        }
    }
}
