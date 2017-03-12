package com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.StructureCreator;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import javafx.util.Pair;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 24.02.2017.
 */
public class MyDatabaseStructureCreator implements StructureCreator {

    private static final String EMPLOYEE_TABLE_NAME = "Employee";
    private static final String PROJECT_TABLE_NAME = "Project";
    private static final String ROLE_TABLE_NAME = "Role";
    private static final String SPRINT_TABLE_NAME = "Sprint";
    private static final String TASK_TABLE_NAME = "Task";
    private static final String TASK_CONFIRMATION_TABLE_NAME = "TaskConfirmation";
    private static final String TIME_REQUEST_TABLE_NAME = "TimeRequest";
    private static final String USER_TABLE_NAME = "User";

    private static final String USER_ROLE_TABLE_NAME = "user_role";
    private static final String TASK_DEPENDENCY_TABLE_NAME = "task_dependency";


    private List<Pair<String, DataType>> userProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("firstName", DataType.STRING));
        add(new Pair<>("lastName", DataType.STRING));
        add(new Pair<>("login", DataType.STRING));
        add(new Pair<>("password", DataType.STRING));
    }};

    private List<Pair<String, DataType>> userRoleProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("userId", DataType.LONG));
        add(new Pair<>("roleId", DataType.LONG));
    }};

    private List<Pair<String, DataType>> employeeProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("position", DataType.STRING));
        add(new Pair<>("userId_extend", DataType.LONG));
    }};

    private List<Pair<String, DataType>> projectProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("customerId", DataType.LONG));
        add(new Pair<>("name", DataType.STRING));
        add(new Pair<>("startDate", DataType.DATE));
        add(new Pair<>("endDate", DataType.DATE));
        add(new Pair<>("plannedStartDate", DataType.DATE));
        add(new Pair<>("plannedEndDate", DataType.DATE));
        add(new Pair<>("managerId", DataType.LONG));
    }};

    private List<Pair<String, DataType>> roleProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("name", DataType.STRING));
    }};

    private List<Pair<String, DataType>> sprintProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("name", DataType.STRING));
        add(new Pair<>("startDate", DataType.DATE));
        add(new Pair<>("endDate", DataType.DATE));
        add(new Pair<>("plannedStartDate", DataType.DATE));
        add(new Pair<>("plannedEndDate", DataType.DATE));
        add(new Pair<>("finished", DataType.BOOLEAN));
        add(new Pair<>("nextSprintId", DataType.LONG));
        add(new Pair<>("previousSprintId", DataType.LONG));
        add(new Pair<>("projectId", DataType.LONG));
    }};

    private List<Pair<String, DataType>> taskProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("name", DataType.STRING));
        add(new Pair<>("parentTaskId", DataType.LONG));
        add(new Pair<>("estimateTime", DataType.INTEGER));
        add(new Pair<>("executionTime", DataType.INTEGER));
        add(new Pair<>("sprintId", DataType.LONG));
        add(new Pair<>("status", DataType.STRING));
        add(new Pair<>("requiredPosition", DataType.STRING));
    }};

    private List<Pair<String, DataType>> taskDependencyProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("taskId", DataType.LONG));
        add(new Pair<>("dependencyId", DataType.LONG));
    }};

    private List<Pair<String, DataType>> taskConfirmationProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("employeeId", DataType.LONG));
        add(new Pair<>("taskId", DataType.LONG));
        add(new Pair<>("status", DataType.STRING));
    }};

    private List<Pair<String, DataType>> timeRequestProperties = new ArrayList<Pair<String, DataType>>() {{
        add(new Pair<>("taskId", DataType.LONG));
        add(new Pair<>("reason", DataType.STRING));
        add(new Pair<>("requestTime", DataType.INTEGER));
        add(new Pair<>("responseTime", DataType.INTEGER));
        add(new Pair<>("status", DataType.STRING));
    }};

    @Override
    public void createTaskManagerStructure(Database database) {
        createTableStructure(database);
        insertDefaultRoles(database);
    }

    private void createTableStructure(Database database) {
        database.createTable(USER_TABLE_NAME, userProperties);
        database.createTable(EMPLOYEE_TABLE_NAME, employeeProperties);
        database.createTable(PROJECT_TABLE_NAME, projectProperties);
        database.createTable(ROLE_TABLE_NAME, roleProperties);
        database.createTable(USER_ROLE_TABLE_NAME, userRoleProperties);
        database.createTable(SPRINT_TABLE_NAME, sprintProperties);
        database.createTable(TASK_TABLE_NAME, taskProperties);
        database.createTable(TASK_DEPENDENCY_TABLE_NAME, taskDependencyProperties);
        database.createTable(TASK_CONFIRMATION_TABLE_NAME, taskConfirmationProperties);
        database.createTable(TIME_REQUEST_TABLE_NAME, timeRequestProperties);

    }

    private void insertDefaultRoles(Database database) {
        for (Role role: Role.values()) {
            List<Pair<String, Object>> values = new ArrayList<>();
            values.add(new Pair<>("name", role.toString()));
            database.insertInto(ROLE_TABLE_NAME, values);
        }
    }


}
