package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.transaction;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception.SQLRuntimeException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Yuriy on 3/17/2017.
 */
@FunctionalInterface
public interface Transaction {

    void span();

    static void tx(ConnectionManager cm, Transaction transaction, int transactionIsolationLevel) {
        Connection conn = cm.getConnection();

        boolean autoCommit;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            if(conn.getTransactionIsolation() != transactionIsolationLevel) {
                conn.setTransactionIsolation(transactionIsolationLevel);
            }

            transaction.span();
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
            }

            throw new SQLRuntimeException("", e);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void tx(ConnectionManager cm, Transaction transaction) {
        tx(cm, transaction, Connection.TRANSACTION_READ_COMMITTED);
    }
}


