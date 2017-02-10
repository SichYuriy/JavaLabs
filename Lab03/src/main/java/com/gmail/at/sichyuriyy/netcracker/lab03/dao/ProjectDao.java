package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface ProjectDao extends AbstractDao<Long, Project> {

    List<Project> findByCustomerId(Long id);
    List<Project> findByManagerId(Long id);
    Project findBySprintId(Long id);

    void updateProjectManager(Long projectId, Long managerId);
    void deleteProjectManager(Long projectId);

    void updateCustomer(Long projectId, Long customerId);
    void deleteCustomer(Long projectId);
}
