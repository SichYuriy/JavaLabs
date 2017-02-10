package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class TimeRequestProxy extends TimeRequest {

    private ProjectManagerDao projectManagerDao;
    private TaskDao taskDao;

    private boolean employeeLoaded;
    private boolean taskLoaded;

    private Long managerId;
    private Long taskId;

    public TimeRequestProxy(ProjectManagerDao projectManagerDao, TaskDao taskDao) {
        this.projectManagerDao = projectManagerDao;
        this.taskDao = taskDao;
    }

    @Override
    public ProjectManager getManager() {
        if (!employeeLoaded) {
            loadManager();
        }
        return super.getManager();
    }

    @Override
    public void setManager(ProjectManager employee) {
        employeeLoaded = true;
        super.setManager(employee);
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    private void loadManager() {
        ProjectManager manager;
        if (managerId != null) {
            manager = projectManagerDao.findById(managerId);
        } else {
            manager = projectManagerDao.findByTimeRequestId(getId());
        }
        this.setManager(manager);
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
