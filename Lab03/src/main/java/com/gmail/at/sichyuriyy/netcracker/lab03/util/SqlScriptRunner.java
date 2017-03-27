package com.gmail.at.sichyuriyy.netcracker.lab03.util;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.ConnectionManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception.SQLRuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 3/21/2017.
 */
public class SqlScriptRunner {

    private ConnectionManager connectionManager;

    public SqlScriptRunner(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void executeSqlScript(File file) {
        executeSqlScript(file, "\\s*;\\s*");
    }

    public void executePlSqlScript(File file) {
        executeSqlScript(file, "\\s*/\\s*");
    }


    private void executeSqlScript(File file, String delimiter) {
        int line = 0;
        try {
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                List<String> queries = Arrays.stream(content.split(delimiter))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                Connection conn = connectionManager.getConnection();

                try {
                    for (String query : queries) {
                        try (Statement stmt = conn.createStatement()) {
                            line++;
                            stmt.execute(query.trim());
                        }
                    }
                } finally {
                    tryClose(conn);
                }
            }


        } catch (IOException e) {
            throw new SQLRuntimeException("SQL script file not found", e);
        }catch (SQLException e) {
            throw new SQLRuntimeException("Errors while executing SQL script at #" + line +
                    " in file " + file.getAbsolutePath(), e);
        }
    }

    private void tryClose(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
