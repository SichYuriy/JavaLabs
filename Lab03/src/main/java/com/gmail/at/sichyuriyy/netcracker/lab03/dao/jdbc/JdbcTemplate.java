package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception.SQLRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy on 3/16/2017.
 */
public class JdbcTemplate {

    private ConnectionManager connectionManager;

    public JdbcTemplate(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public <T> T queryObject(String sql, EntityExtractor<T> producer, Object... params){
        Object[] result = new Object[1];
        query(sql, (rs) -> {
            List<T> resultList = producer.extract(rs);
            if (!resultList.isEmpty()) {
                result[0] = resultList.get(0);
            }
        }, params);
        return (T) result[0];
    }

    public <T> List<T> queryObjects(String sql, EntityExtractor<T> producer, Object... params) {
        Object[] result = new Object[1];
        query(sql, (rs) -> {
            result[0] = producer.extract(rs);
        }, params);
        return (List<T>) result[0];
    }

    public void update(String sql, Object... params) {
        executeUpdate(sql, params);
    }

    public void insert(String sql, Object... params) {
        executeUpdate(sql, params);
    }

    public Long insertGetId(String sql, Object... params) {
        Connection conn = connectionManager.getConnection();

        try (PreparedStatement stmt
                     = conn.prepareStatement(sql, new String[]{"ENTITY_ID"})) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                return rs.getLong(1);
            }
            return null;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        } finally {
            tryClose(conn);
        }
    }

    private void executeUpdate(String sql, Object... params) {
        Connection conn = connectionManager.getConnection();

        try (PreparedStatement stmt
                     = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        } finally {
            tryClose(conn);
        }
    }

    private void query(String sql, ResultSetFunction operation, Object... params) {
        Connection conn = connectionManager.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            analyzeResultSet(rs, operation);

        } catch (SQLException e) {
            throw new SQLRuntimeException("", e);
        } finally {
            tryClose(conn);
        }
    }

    private void analyzeResultSet(ResultSet rs, ResultSetFunction operation) {
        try {
            operation.apply(rs);
        } catch (SQLException e) {
            throw new SQLRuntimeException("ResultSet function thrown an exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new SQLRuntimeException("Can not close resultSet", e);
            }
        }
    }


    private void tryClose(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }



    @FunctionalInterface
    private interface ResultSetFunction {
        void apply(ResultSet resultSet) throws SQLException;
    }
}
