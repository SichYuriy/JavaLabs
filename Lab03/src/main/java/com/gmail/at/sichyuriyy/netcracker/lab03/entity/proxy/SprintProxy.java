package com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;

import java.util.List;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class SprintProxy extends Sprint {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private SprintDao sprintDao;

    private boolean projectLoaded;
    private boolean tasksLoaded;
    private boolean previousSprintLoaded;
    private boolean nextSprintLoaded;

    private Long projectId;
    private Long previousSprintId;
    private Long nextSprintId;

    public SprintProxy(ProjectDao projectDao, TaskDao taskDao, SprintDao sprintDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
        this.sprintDao = sprintDao;
    }

    @Override
    public Project getProject() {
        if (!projectLoaded) {
            loadProject();
        }
        return super.getProject();
    }

    @Override
    public void setProject(Project project) {
        projectLoaded = true;
        super.setProject(project);
    }

    @Override
    public List<Task> getTasks() {
        if (!tasksLoaded) {
            loadTasks();
        }
        return super.getTasks();
    }

    @Override
    public void setTasks(List<Task> tasks) {
        tasksLoaded = true;
        super.setTasks(tasks);
    }

    @Override
    public Sprint getPreviousSprint() {
        if (!previousSprintLoaded) {
            loadPreviousSprint();
        }
        return super.getPreviousSprint();
    }

    @Override
    public void setPreviousSprint(Sprint previousSprint) {
        previousSprintLoaded = true;
        super.setPreviousSprint(previousSprint);
    }

    @Override
    public Sprint getNextSprint() {
        if (!nextSprintLoaded) {
            loadNextSprint();
        }
        return super.getNextSprint();
    }

    @Override
    public void setNextSprint(Sprint nextSprint) {
        nextSprintLoaded = true;
        super.setNextSprint(nextSprint);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPreviousSprintId() {
        return previousSprintId;
    }

    public void setPreviousSprintId(Long previousSprintId) {
        this.previousSprintId = previousSprintId;
    }

    public Long getNextSprintId() {
        return nextSprintId;
    }

    public void setNextSprintId(Long nextSprintId) {
        this.nextSprintId = nextSprintId;
    }

    private void loadProject() {
        Project project;
        if (projectId != null) {
            project = projectDao.findById(projectId);
        } else {
            project = projectDao.findBySprintId(getId());
        }
        this.setProject(project);
    }
    
    private void loadTasks() {
        this.setTasks(taskDao.findBySprintId(getId()));
    }
    
    private void loadPreviousSprint() {
        Sprint previousSprint;
        if (previousSprintId != null) {
            previousSprint = sprintDao.findById(previousSprintId);
        } else {
            previousSprint = sprintDao.findByNextSprintId(getId());
        }
        this.setPreviousSprint(previousSprint);
    }

    private void loadNextSprint() {
        Sprint nextSprint;
        if (nextSprintId != null) {
            nextSprint = sprintDao.findById(nextSprintId);
        } else {
            nextSprint = sprintDao.findByPreviousSprintId(getId());
        }
        this.setNextSprint(nextSprint);
    }
}
