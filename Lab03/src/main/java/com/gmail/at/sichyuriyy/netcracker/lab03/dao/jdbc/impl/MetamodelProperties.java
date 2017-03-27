package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

/**
 * Created by Yuriy on 3/19/2017.
 */
public interface MetamodelProperties {

    String USER_TYPE = "entity.user";
    String ROLE_TYPE = "entity.role";
    String EMPLOYEE_TYPE = "entity.employee";
    String PROJECT_TYPE = "entity.project";
    String SPRINT_TYPE = "entity.sprint";
    String TASK_TYPE = "entity.task";
    String TASK_CONFIRMATION_TYPE = "entity.taskConfirmation";
    String TIME_REQUEST_TYPE = "entity.timeRequest";

    String POSITION = "attribute.position";
    String FIRST_NAME = "attribute.firstName";
    String LAST_NAME = "attribute.lastName";
    String LOGIN = "attribute.login";
    String PASSWORD = "attribute.password";
    String ROLES = "attribute.roles";
    String CUSTOMER_ID = "attribute.customerId";
    String NAME = "attribute.name";
    String START_DATE = "attribute.startDate";
    String END_DATE = "attribute.endDate";
    String PLANNED_START_DATE = "attribute.plannedStartDate";
    String PLANNED_END_DATE = "attribute.plannedEndDate";
    String MANAGER_ID = "attribute.managerId";
    String FINISHED = "attribute.finished";
    String NEXT_SPRINT_ID = "attribute.nextSprintId";
    String PREVIOUS_SPRINT_ID = "attribute.previousSprintId";
    String PROJECT_ID = "attribute.projectId";
    String PARENT_TASK_ID = "attribute.parentTaskId";
    String ESTIMATE_TIME = "attribute.estimateTime";
    String EXECUTION_TIME = "attribute.executionTime";
    String SPRINT_ID = "attribute.sprintId";
    String STATUS = "attribute.status";
    String REQUIRED_POSITION = "attribute.requiredPosition";
    String DEPENDENCIES = "attribute.dependencies";
    String EMPLOYEE_ID = "attribute.employeeId";
    String TASK_ID = "attribute.taskId";
    String REASON = "attribute.reason";
    String REQUEST_TIME = "attribute.requestTime";
    String RESPONSE_TIME = "attribute.responseTime";

    String ADMINISTRATOR_ROLE = "role.administrator";
    String PROJECT_MANAGER_ROLE = "role.projectManager";
    String EMPLOYEE_ROLE = "role.employee";
    String CUSTOMER_ROLE = "role.customer";
}
