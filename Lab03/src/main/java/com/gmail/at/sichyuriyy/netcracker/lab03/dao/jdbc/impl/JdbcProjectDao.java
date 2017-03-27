package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.JdbcTemplate;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.ProjectExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.PropertiesLoader;

import java.util.List;
import java.util.Properties;

/**
 * Created by Yuriy on 3/20/2017.
 */
public class JdbcProjectDao extends AbstractJdbcDao implements ProjectDao {

    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;
    private SprintDao sprintDao;

    public JdbcProjectDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void create(Project project) {
        Transaction.tx(connectionManager, ()-> {
            long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                    metamodelProperties.get(MetamodelProperties.PROJECT_TYPE));
            jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.NAME),
                    project.getName());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.START_DATE),
                    project.getStartDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.END_DATE),
                    project.getEndDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.PLANNED_START_DATE),
                    project.getPlannedStartDate());
            jdbcTemplate.insert(SqlRequest.INSERT_DATE_VALUE,
                    id, metamodelProperties.getProperty(MetamodelProperties.PLANNED_END_DATE),
                    project.getPlannedEndDate());
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    id, metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ID),
                    project.getCustomer().getId());
            if (project.getProjectManager() != null) {
                jdbcTemplate.insert(SqlRequest.INSERT_REF,
                        id, metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID),
                        project.getProjectManager().getId());
            }
            project.setId(id);
        });
    }

    @Override
    public Project findById(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_ID,
                new ProjectExtractor(projectManagerDao, customerDao, sprintDao),
                id, metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE));
    }

    @Override
    public List<Project> findAll() {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_ALL,
                new ProjectExtractor(projectManagerDao, customerDao, sprintDao),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE));
    }

    @Override
    public void update(Project project) {
        Transaction.tx(connectionManager, () -> {
           jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, project.getName(), project.getId(),
                   metamodelProperties.getProperty(MetamodelProperties.NAME));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, project.getStartDate(), project.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.START_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, project.getEndDate(), project.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.END_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, project.getPlannedStartDate(), project.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.PLANNED_START_DATE));
            jdbcTemplate.update(SqlRequest.UPDATE_DATE_VALUE, project.getPlannedEndDate(), project.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.PLANNED_END_DATE));
        });
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public List<Project> findByCustomerId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new ProjectExtractor(projectManagerDao, customerDao, sprintDao),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ID),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE));
    }

    @Override
    public List<Project> findByManagerId(Long id) {
        return jdbcTemplate.queryObjects(SqlRequest.SELECT_BY_REF_ID,
                new ProjectExtractor(projectManagerDao, customerDao, sprintDao),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE));
    }

    @Override
    public Project findBySprintId(Long id) {
        return jdbcTemplate.queryObject(SqlRequest.SELECT_BY_OWNER_REF_ID,
                new ProjectExtractor(projectManagerDao, customerDao, sprintDao),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                id, metamodelProperties.getProperty(MetamodelProperties.PROJECT_ID),
                metamodelProperties.getProperty(MetamodelProperties.PROJECT_TYPE));
    }

    @Override
    public void updateProjectManager(Long projectId, Long managerId) {
        Transaction.tx(connectionManager, () -> {
           deleteProjectManager(projectId);
           jdbcTemplate.insert(SqlRequest.INSERT_REF,
                   projectId,
                   metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID),
                   managerId);
        });
    }

    @Override
    public void deleteProjectManager(Long projectId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE,
                projectId, metamodelProperties.getProperty(MetamodelProperties.MANAGER_ID));
    }

    @Override
    public void updateCustomer(Long projectId, Long customerId) {
        Transaction.tx(connectionManager, () -> {
            deleteCustomer(projectId);
            jdbcTemplate.insert(SqlRequest.INSERT_REF,
                    projectId,
                    metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ID),
                    customerId);
        });
    }

    @Override
    public void deleteCustomer(Long projectId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE,
                projectId, metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ID));
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
