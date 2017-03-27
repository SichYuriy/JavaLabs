package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Yuriy on 3/16/2017.
 */
public class ConnectionManagerTxProxy extends ConnectionManager {

    private static int conn_number=1;

    private ConnectionManager connectionManager;

    private final ThreadLocal<ConnectionTxProxy> connThreadLocal = new ThreadLocal<>();

    public ConnectionManagerTxProxy(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Connection getConnection() {
        if (connThreadLocal.get() != null) {
            ConnectionTxProxy conn = connThreadLocal.get();
            conn.borrow();
            return conn;
        }

        ConnectionTxProxy conn = new ConnectionTxProxy(connectionManager.getConnection(), this);
        conn.borrow();
        connThreadLocal.set(conn);
        return conn;
    }

    void removeLocalConnection() throws SQLException {
        try {
            ConnectionTxProxy conn = connThreadLocal.get();
            conn.getConnection().close();
            System.out.println("close connection # " + conn_number++);
        } finally {
            connThreadLocal.remove();
        }
    }



}
