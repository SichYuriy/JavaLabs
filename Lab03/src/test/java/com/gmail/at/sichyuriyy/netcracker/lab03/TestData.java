package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 25.02.2017.
 */
public class TestData {

    public static Customer getCustomer(){
        return getCustomer("firstName");
    }

    public static Customer getCustomer(String firstName) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.CUSTOMER);
        Customer customer = new Customer();
        customer.setRoles(roles);
        customer.setFirstName(firstName);
        customer.setLastName("lastName");
        customer.setLogin("login");
        customer.setPassword("123321");
        return customer;
    }

    public static Project getProject(Customer customer, ProjectManager projectManager) {
        return getProject("projectName", customer, projectManager);
    }
    
    public static Project getProject(String name, Customer customer, ProjectManager projectManager) {
        Project project = new Project();
        project.setCustomer(customer);
        project.setName(name);
        project.setStartDate(new Date(0));
        project.setEndDate(new Date(100));
        project.setPlannedStartDate(new Date(0));
        project.setPlannedEndDate(new Date(100));
        project.setProjectManager(projectManager);

        return project;
    }
    
    public static Employee getEmployee(String firstName) {
        Employee employee = new Employee();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.EMPLOYEE);
        employee.setRoles(roles);
        employee.setFirstName(firstName);
        employee.setLastName("lastName");
        employee.setLogin("login");
        employee.setPassword("123321");
        employee.setPosition(Employee.EmployeePosition.JUNIOR);
        return employee; 
    }

    public static Employee getEmployee() {
        return getEmployee("firstNameEMP");
    }
    
    public static ProjectManager getProjectManager(String firstName) {
        ProjectManager projectManager = new ProjectManager();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.EMPLOYEE);
        roles.add(Role.PROJECT_MANAGER);
        projectManager.setRoles(roles);
        projectManager.setFirstName(firstName);
        projectManager.setLastName("lastName");
        projectManager.setLogin("login");
        projectManager.setPassword("123321");
        projectManager.setPosition(Employee.EmployeePosition.SENIOR);
        return projectManager;
    }

    public static ProjectManager getProjectManager() {
        return getProjectManager("firstNamePM");
    }

    public static TaskConfirmation getTaskConfirmation(Employee employee, Task task) {
        TaskConfirmation taskConfirmation = new TaskConfirmation();
        taskConfirmation.setEmployee(employee);
        taskConfirmation.setTask(task);
        taskConfirmation.setStatus(TaskConfirmation.ConfirmationStatus.UNCONFIRMED);
        return taskConfirmation;
    }

    public static Sprint getSprint(String name, Project project) {
        Sprint sprint = new Sprint();
        sprint.setFinished(false);
        sprint.setName(name);
        sprint.setProject(project);
        sprint.setStartDate(new Date(0));
        sprint.setEndDate(new Date(100));
        sprint.setPlannedStartDate(new Date(0));
        sprint.setPlannedEndDate(new Date(100));
        return sprint;
    }

    public static Sprint getSprint(Project project) {
        return getSprint("sprintName", project);
    }


    public static Task getTask(String name, Sprint sprint) {
        Task task = new Task();
        task.setName(name);
        task.setSprint(sprint);
        task.setStatus(Task.TaskStatus.PLANED);
        task.setRequiredPosition(Employee.EmployeePosition.JUNIOR);
        task.setExecutionTime(23);
        task.setEstimateTime(23);
        return task;
    }

    public static Task getTask(Sprint sprint) {
        return getTask("taskName", sprint);
    }

    public static TimeRequest getTimeRequest(ProjectManager projectManager, Task task) {
        TimeRequest timeRequest = new TimeRequest();
        timeRequest.setStatus(TimeRequest.Status.ACCEPTED);
        timeRequest.setManager(projectManager);
        timeRequest.setTask(task);
        timeRequest.setReason("slowpoke");
        timeRequest.setRequestTime(24);
        timeRequest.setResponseTime(20);
        return timeRequest;
    }

    public static EmployeeTestData getEmployeeTestData(DatabaseConnector databaseConnector) {
        Sprint fakeSprint = FakeData.getSprint(1L);
        Task task1 = getTask(fakeSprint);
        Task task2 = getTask(fakeSprint);

        databaseConnector.getTaskDao().create(task1);
        databaseConnector.getTaskDao().create(task2);

        Employee confirmationEmployee = getEmployee("confirmationEmployee");
        TaskConfirmation taskConfirmation = getTaskConfirmation(confirmationEmployee, task1);

        RelationUtils.addTaskConfirmation(confirmationEmployee, taskConfirmation);
        databaseConnector.getEmployeeDao().create(confirmationEmployee);
        databaseConnector.getTaskConfirmationDao().create(taskConfirmation);

        return new EmployeeTestData(task1, task2, confirmationEmployee, taskConfirmation);
    }

    public static class EmployeeTestData {

        public Task task1;
        public Task task2;

        public Employee confirmationEmployee;
        public TaskConfirmation taskConfirmation;

        public EmployeeTestData(Task task1, Task task2,
                                Employee confirmationEmployee,
                                TaskConfirmation taskConfirmation) {
            this.task1 = task1;
            this.task2 = task2;
            this.confirmationEmployee = confirmationEmployee;
            this.taskConfirmation = taskConfirmation;
        }
    }




    
    

}
