package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;

/**
 * Created by Yuriy on 26.01.2017.
 */
public interface ProjectManagerDao extends AbstractDao<Long, ProjectManager> {

    ProjectManager findByProjectId(Long id);
}
