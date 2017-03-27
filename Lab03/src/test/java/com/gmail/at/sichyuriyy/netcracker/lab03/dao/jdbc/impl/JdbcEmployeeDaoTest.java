package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDaoTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.factory.JdbcDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl.JdbcDatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.PropertiesLoader;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.ResourceUtil;
import com.gmail.at.sichyuriyy.netcracker.lab03.util.SqlScriptRunner;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 3/24/2017.
 */
public class JdbcEmployeeDaoTest extends EmployeeDaoTest {

    private DatabaseConnector databaseConnector;
    private SqlScriptRunner sqlScriptRunner;

    public JdbcEmployeeDaoTest() {
        ConnectionManager connectionManager = ConnectionManager.fromProperties(
                PropertiesLoader.getInstance().loadProperties("testdb.properties"));
        JdbcDaoFactory daoFactory = new JdbcDaoFactory(connectionManager);
        sqlScriptRunner = new SqlScriptRunner(connectionManager);
        databaseConnector = new JdbcDatabaseConnector(daoFactory);
    }

    @Override
    protected DatabaseConnector getTestedDatabaseConnector() {
        return databaseConnector;
    }

    @Override
    protected void cleanResources() {
        sqlScriptRunner.executeSqlScript(ResourceUtil.getResourceFile("refresh_database.sql"));
    }
}