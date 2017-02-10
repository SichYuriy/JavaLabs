package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskConfirmationDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class TaskProxy extends Task {

    private EmployeeDao employeeDao;
    private TaskDao taskDao;
    private TaskConfirmationDao taskConfirmationDao;
    private SprintDao sprintDao;

    private boolean parentTaskLoaded;
    private boolean employeesLoaded;
    private boolean childTasksLoaded;
    private boolean dependenciesLoaded;
    private boolean taskConfirmationsLoaded;
    private boolean sprintLoaded;

    private Long parentTaskId;
    private Long sprintId;

    public TaskProxy(EmployeeDao employeeDao, TaskDao taskDao,
                     TaskConfirmationDao taskConfirmationDao,
                     SprintDao sprintDao) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.sprintDao = sprintDao;
    }

    @Override
    public Task getParentTask() {
        if (!parentTaskLoaded) {
            loadParentTask();
        }
        return super.getParentTask();
    }

    @Override
    public void setParentTask(Task parentTask) {
        parentTaskLoaded = true;
        super.setParentTask(parentTask);
    }

    @Override
    public List<Employee> getEmployees() {
        if (!employeesLoaded) {
            loadEmployees();
        }
        return super.getEmployees();
    }

    @Override
    public void setEmployees(List<Employee> employees) {
        employeesLoaded = true;
        super.setEmployees(employees);
    }

    @Override
    public List<Task> getChildTasks() {
        if (!childTasksLoaded) {
            loadChildTasks();
        }
        return super.getChildTasks();
    }

    @Override
    public void setChildTasks(List<Task> childTasks) {
        childTasksLoaded = true;
        super.setChildTasks(childTasks);
    }

    @Override
    public List<Task> getDependencies() {
        if (!dependenciesLoaded) {
            loadDependencies();
        }
        return super.getDependencies();
    }

    @Override
    public void setDependencies(List<Task> dependencies) {
        dependenciesLoaded = true;
        super.setDependencies(dependencies);
    }

    @Override
    public List<TaskConfirmation> getTaskConfirmations() {
        if (!taskConfirmationsLoaded) {
            loadTaskConfirmations();
        }
        return super.getTaskConfirmations();
    }

    @Override
    public void setTaskConfirmations(List<TaskConfirmation> taskConfirmations) {
        taskConfirmationsLoaded = true;
        super.setTaskConfirmations(taskConfirmations);
    }

    @Override
    public Sprint getSprint() {
        if (!sprintLoaded) {
            loadSprint();
        }
        return super.getSprint();
    }

    @Override
    public void setSprint(Sprint sprint) {
        sprintLoaded = true;
        super.setSprint(sprint);
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    private void loadEmployees() {
        this.setEmployees(employeeDao.findByTaskId(getId()));
    }

    private void loadChildTasks() {
        this.setChildTasks(taskDao.findChildTasksByParentTaskId(getId()));
    }

    private void loadDependencies() {
        this.setDependencies(taskDao.findDependenciesByParentTaskId(getId()));
    }

    private void loadTaskConfirmations() {
        this.setTaskConfirmations(taskConfirmationDao.findByTaskId(getId()));
    }

    private void loadSprint() {
        Sprint sprint;
        if (sprintId != null) {
            sprint = sprintDao.findById(sprintId);
        } else {
            sprint = sprintDao.findByTaskId(getId());
        }
        this.setSprint(sprint);
    }

    private void loadParentTask() {
        Task task;
        if (parentTaskId != null) {
            task = taskDao.findById(parentTaskId);
        } else {
            task = taskDao.findByChildTaskId(getId());
        }
        this.setParentTask(task);
    }
}
