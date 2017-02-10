package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class TaskConfirmationProxy extends TaskConfirmation {

    private EmployeeDao employeeDao;
    private TaskDao taskDao;

    private boolean employeeLoaded;
    private boolean taskLoaded;
    
    private Long employeeId;
    private Long taskId;

    public TaskConfirmationProxy(EmployeeDao employeeDao, TaskDao taskDao) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
    }

    @Override
    public Employee getEmployee() {
        if (!employeeLoaded) {
            loadEmployee();
        }
        return super.getEmployee();
    }

    @Override
    public void setEmployee(Employee employee) {
        employeeLoaded = true;
        super.setEmployee(employee);
    }

    @Override
    public Task getTask() {
        if (!taskLoaded) {
            loadTask();
        }
        return super.getTask();
    }

    @Override
    public void setTask(Task task) {
        taskLoaded = true;
        super.setTask(task);
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    private void loadEmployee() {
        Employee employee;
        if (employeeId != null) {
            employee = employeeDao.findById(employeeId);
        } else {
            employee = employeeDao.findByTaskConfirmationId(getId());
        }
        this.setEmployee(employee);
    }

    private void loadTask() {
        Task task;
        if (taskId != null) {
            task = taskDao.findById(taskId);
        } else {
            task = taskDao.findByTaskConfirmationId(getId());
        }
        this.setTask(task);
    }
}
