package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yuriy on 26.02.2017.
 */
public class RelationUtils {

    public static void addOrderedProject(Customer customer, Project project) {
        List<Project> orderedProjects = customer.getOrderedProjects();
        if (orderedProjects == null) {
            orderedProjects = new ArrayList<>();
        }
        orderedProjects.add(project);
        customer.setOrderedProjects(orderedProjects);
    }

    public static void addManagedProject(ProjectManager projectManager, Project project) {
        List<Project> managedProjects = projectManager.getManagedProjects();
        if (managedProjects == null) {
            managedProjects = new ArrayList<>();
        }
        managedProjects.add(project);
        projectManager.setManagedProjects(managedProjects);
    }

    public static void addTaskConfirmation(Employee employee, TaskConfirmation taskConfirmation) {
        List<TaskConfirmation> taskConfirmations = employee.getTaskConfirmations();
        if (taskConfirmations == null) {
            taskConfirmations = new ArrayList<>();
        }
        taskConfirmations.add(taskConfirmation);
        employee.setTaskConfirmations(taskConfirmations);
    }

    public static void addTaskEmployeeRelation(Task task, Employee ...employees) {
        List<Employee> taskEmployees = task.getEmployees();
        if (taskEmployees == null) {
            taskEmployees = new ArrayList<>();
        }

        for (Employee employee: employees) {
            taskEmployees.add(employee);

            List<Task> tasks = employee.getTasks();
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
            tasks.add(task);
            employee.setTasks(tasks);
        }

        task.setEmployees(taskEmployees);
    }

    public static void addRoles(User user, Role ...roles) {
        List<Role> userRoles = user.getRoles();
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }
        Collections.addAll(userRoles, roles);
        user.setRoles(userRoles);
    }

    public static void addSprints(Project project, Sprint ...sprints) {
        List<Sprint> projectSprints = project.getSprints();
        if (projectSprints == null) {
            projectSprints = new ArrayList<>();
        }
        Collections.addAll(projectSprints, sprints);
        project.setSprints(projectSprints);
    }

    public static void addTasks(Sprint sprint, Task ...tasks) {
        List<Task> sprintTasks = sprint.getTasks();
        if (sprintTasks == null) {
            sprintTasks = new ArrayList<>();
        }
        Collections.addAll(sprintTasks, tasks);
        sprint.setTasks(sprintTasks);
    }

    public static void addDependencies(Task task, Task ...dependencies) {
        List<Task> taskDependencies = task.getDependencies();
        if (taskDependencies == null) {
            taskDependencies = new ArrayList<>();
        }
        Collections.addAll(taskDependencies, dependencies);
        task.setDependencies(taskDependencies);
    }

    public static void addTimeRequests(Task task, TimeRequest ...requests) {
        List<TimeRequest> taskRequests = task.getTimeRequests();
        if (taskRequests == null) {
            taskRequests = new ArrayList<>();
        }
        Collections.addAll(taskRequests, requests);
        task.setTimeRequests(taskRequests);
    }

    public static void addTaskConfirmations(Task task, TaskConfirmation ...confirmations) {
        List<TaskConfirmation> taskConfirmations = task.getTaskConfirmations();
        if (taskConfirmations == null) {
            taskConfirmations = new ArrayList<>();
        }
        Collections.addAll(taskConfirmations, confirmations);
        task.setTaskConfirmations(taskConfirmations);
    }

    public static void addChildTasksRelation(Task parent, Task ...childTasks) {
        List<Task> parentChildTasks = parent.getChildTasks();
        if (parentChildTasks == null) {
            parentChildTasks = new ArrayList<>();
        }
        Collections.addAll(parentChildTasks, childTasks);
        parent.setChildTasks(parentChildTasks);
    }


}
