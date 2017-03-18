package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception.SQLRuntimeException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by Yuriy on 3/16/2017.
 */
public class ConnectionManager {

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
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLRuntimeException("can not get connection", e);
        }
    }
}
