package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception.SQLRuntimeException;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction.ConnectionManagerTxProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by Yuriy on 3/16/2017.
 */
public class ConnectionManager {

    private static int conn_number = 1;

    private String url;
    private String user;
    private String password;

    static {
        try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLRuntimeException("can not load jdbcDriver", e);
        }
    }

    public ConnectionManager() {
    }

    public ConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            System.out.println("get connection # " + conn_number++);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLRuntimeException("can not get connection", e);
        }
    }

    public static ConnectionManager fromProperties(Properties properties) {
        ConnectionManager connectionManager = new ConnectionManager(
                properties.getProperty("database.url"),
                properties.getProperty("database.user"),
                properties.getProperty("database.password")
        );
        ConnectionManagerTxProxy proxy = new ConnectionManagerTxProxy(connectionManager);
        return proxy;
    }


}
