package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor.RoleExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/19/2017.
 */
public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private static final String SELECT_ROLES_BY_USER_ID = SqlRequest.SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.owner_id=ref_entity.entity_id AND (ref_entity.entity_type_id=? OR ref_entity.entity_type_id=?)) " +
            "INNER JOIN entity ON (filter_refs.owner_id=? AND filter_refs.entity_id=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
            SqlRequest.SELECT_END;

    private Map<Role, Long> roleMap = new HashMap<Role, Long>() {{
        put(Role.ADMINISTRATOR, Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.ADMINISTRATOR_ROLE)));
        put(Role.PROJECT_MANAGER, Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.PROJECT_MANAGER_ROLE)));
        put(Role.EMPLOYEE, Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_ROLE)));
        put(Role.CUSTOMER, Long.valueOf(metamodelProperties.getProperty(MetamodelProperties.CUSTOMER_ROLE)));
    }};

    private EmployeeDao employeeDao;
    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;

    public JdbcUserDao(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    protected void createUser(User user, Long userTypeId) {
        Transaction.tx(connectionManager,
                () -> {
                    long id = jdbcTemplate.insertGetId(SqlRequest.INSERT_ENTITY,
                            userTypeId);
                    jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                            id, metamodelProperties.getProperty(MetamodelProperties.FIRST_NAME), user.getFirstName());
                    jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                            id, metamodelProperties.getProperty(MetamodelProperties.LAST_NAME), user.getLastName());
                    jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                            id, metamodelProperties.getProperty(MetamodelProperties.LOGIN), user.getLogin());
                    jdbcTemplate.insert(SqlRequest.INSERT_TEXT_VALUE,
                            id, metamodelProperties.getProperty(MetamodelProperties.PASSWORD), user.getPassword());
                    addRoles(id, user.getRoles());
                    user.setId(id);
                });
    }

    @Override
    public void update(User user) {
        Transaction.tx(connectionManager, () -> {
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, user.getFirstName(), user.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.FIRST_NAME));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, user.getLastName(), user.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.LAST_NAME));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, user.getLogin(), user.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.LOGIN));
            jdbcTemplate.update(SqlRequest.UPDATE_TEXT_VALUE, user.getPassword(), user.getId(),
                    metamodelProperties.getProperty(MetamodelProperties.PASSWORD));
        });
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return jdbcTemplate.queryObjects(SELECT_ROLES_BY_USER_ID,
                new RoleExtractor(),
                metamodelProperties.getProperty(MetamodelProperties.USER_TYPE),
                metamodelProperties.getProperty(MetamodelProperties.EMPLOYEE_TYPE),
                userId, metamodelProperties.get(MetamodelProperties.ROLES),
                metamodelProperties.getProperty(MetamodelProperties.ROLE_TYPE));
    }

    @Override
    public void updateRoles(Long userId, List<Role> roles) {
        Transaction.tx(connectionManager, () -> {
            deleteAllRoles(userId);
            addRoles(userId, roles);
        });
    }

    @Override
    public void addRole(Long userId, Role role) {
        jdbcTemplate.insert(SqlRequest.INSERT_REF,
                userId, metamodelProperties.getProperty(MetamodelProperties.ROLES),
                roleMap.get(role));
    }

    @Override
    public void addRoles(Long userId, List<Role> roles) {
        Transaction.tx(connectionManager, ()->{
            for (Role role: roles) {
                addRole(userId, role);
            }
        });
    }

    @Override
    public void deleteRole(Long userId, Role role) {
        jdbcTemplate.update(SqlRequest.DELETE_REF,
                userId, metamodelProperties.getProperty(MetamodelProperties.ROLES),
                roleMap.get(role));
    }

    @Override
    public void deleteAllRoles(Long userId) {
        jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE,
                userId, metamodelProperties.getProperty(MetamodelProperties.ROLES));
    }


    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
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
}
