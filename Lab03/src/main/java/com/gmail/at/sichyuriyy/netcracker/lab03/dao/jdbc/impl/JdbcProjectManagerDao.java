package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.ProjectManagerExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;

import java.util.List;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcProjectManagerDao extends AbstractJdbcDao implements ProjectManagerDao {

    private static final String SELECT_PROJECT_MANAGER_BY_REF_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.entity_id=ref_entity.entity_id AND ref_entity.entity_type_id=?) " +
            "INNER JOIN entity ON (filter_refs.ENTITY_ID=? AND filter_refs.OWNER_ID=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity.entity_type_id=? AND (entity_type.entity_type_id=entity.entity_type_id OR entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_PROJECT_MANAGER_BY_OWNER_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.owner_id=ref_entity.entity_id AND ref_entity.entity_type_id=?) " +
            "INNER JOIN entity ON (filter_refs.owner_id=? AND filter_refs.entity_id=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity.entity_type_id=? AND (entity_type.entity_type_id=entity.entity_type_id OR entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_PROJECT_MANAGER_BY_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.entity_id=ref_entity.entity_id AND ref_entity.entity_type_id=?) " +
            "INNER JOIN entity ON (entity.entity_id=? AND filter_refs.ENTITY_ID=? AND filter_refs.OWNER_ID=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity.entity_type_id=? AND (entity_type.entity_type_id=entity.entity_type_id OR entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;

    private static final String SELECT_PROJECT_MANAGER_BY_TIME_REQUEST_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs req_task_refs " +
            "INNER JOIN entity req_entity ON (req_task_refs.owner_id=? AND req_task_refs.attribute_id=? AND req_task_refs.owner_id=req_entity.entity_id AND req_entity.entity_type_id=?) " +
            "INNER JOIN refs task_sprint_refs ON (req_task_refs.entity_id=task_sprint_refs.owner_id AND task_sprint_refs.attribute_id=?) " +
            "INNER JOIN refs sprint_project_refs ON (task_sprint_refs.entity_id=sprint_project_refs.owner_id AND sprint_project_refs.attribute_id=?) " +
            "INNER JOIN refs project_manager_refs ON (sprint_project_refs.entity_id=project_manager_refs.owner_id AND project_manager_refs.attribute_id=?) " +
            "INNER JOIN entity ON (project_manager_refs.entity_id=entity.entity_id) " +
            "INNER JOIN entity_type ON (entity.entity_type_id=? AND (entity_type.entity_type_id=entity.entity_type_id OR entity_type.entity_type_id=?)) " +
            SqlRequest.SELECT_END;


    private TaskConfirmationDao taskConfirmationDao;
    private TaskDao taskDao;
    private ProjectDao projectDao;
    private EmployeeDao employeeDao;
    private JdbcUserDao userDao;

    public JdbcProjectManagerDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(ProjectManager manager) {
        employeeDao.create(manager);
    }

    @Override
    public ProjectManager findById(Long id) {
        return jdbcTemplate.queryObject(SELECT_PROJECT_MANAGER_BY_ID,
                new ProjectManagerExtractor(projectDao, taskDao, taskConfirmationDao, userDao),
                metamodelProperties.getProperty(MetamodelProperties.ROLE_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PROJECT_MANAGER_ROLE),
                metamodelProperties.getProperty(MetamodelProperties.ROLES),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    @Override
    public List<ProjectManager> findAll() {
        return jdbcTemplate.queryObjects(SELECT_PROJECT_MANAGER_BY_REF_ID,
                new ProjectManagerExtractor(projectDao, taskDao, taskConfirmationDao, userDao),
                metamodelProperties.getProperty(MetamodelProperties.ROLE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_MANAGER_ROLE),
                metamodelProperties.getProperty(MetamodelProperties.ROLES),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    @Override
    public void update(ProjectManager manager) {
        employeeDao.update(manager);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public ProjectManager findByProjectId(Long id) {
        return jdbcTemplate.queryObject(SELECT_PROJECT_MANAGER_BY_OWNER_ID,
                new ProjectManagerExtractor(projectDao, taskDao, taskConfirmationDao, userDao),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE),
                id,
                metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
    }

    @Override
    public ProjectManager findByTimeRequestId(Long id) {
        return jdbcTemplate.queryObject(SELECT_PROJECT_MANAGER_BY_TIME_REQUEST_ID,
                new ProjectManagerExtractor(projectDao, taskDao, taskConfirmationDao, userDao),
                id, metamodelProperties.getProperty(MetamodelProperties.TASK_ID),
                metamodelProperties.getProperty(MetamodelProperties.TASK_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.SPRINT_ID),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_ID),
                metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE));
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

    public JdbcUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(JdbcUserDao userDao) {
        this.userDao = userDao;
    }
}
