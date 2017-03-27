package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.JdbcTemplate;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.Transaction;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.PropertiesLoader;

import java.util.Properties;

/**
 * Created by Yuriy on 3/20/2017.
 */
public abstract class AbstractJdbcDao {

    protected ConnectionManager connectionManager;
    protected JdbcTemplate jdbcTemplate;

    protected Properties metamodelProperties = PropertiesLoader.getInstance().getMetamodelProperties();

    public AbstractJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    protected void delete(Long id) {
        Transaction.tx(connectionManager, () -> {
            jdbcTemplate.update(SqlRequest.DELETE_ALL_REFS_BY_OWNER, id);
            jdbcTemplate.update(SqlRequest.DELETE_ALL_VALUES_BY_ENTITY_ID, id);
            jdbcTemplate.update(SqlRequest.DELETE_ENTITY, id);
        });
    }

}
