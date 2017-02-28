package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;

/**
 * Created by Yuriy on 26.02.2017.
 */
public class FakeData {

    public static Customer getCustomer(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    public static Task getTask(Long id) {
        Task task = new Task();
        task.setId(id);
        return task;
    }

    public static Sprint getSprint(Long id) {
        Sprint sprint = new Sprint();
        sprint.setId(id);
        return sprint;
    }

    public static ProjectManager getProjectManager(Long id) {
        ProjectManager projectManager = new ProjectManager();
        projectManager.setId(id);
        return projectManager;
    }

    public static Employee getEmployee(Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        return  employee;
    }

    public static Project getProject(Long id) {
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
