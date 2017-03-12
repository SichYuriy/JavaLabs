package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 06.02.2017.
 */
public class ProjectManagerMapper implements RecordMapper<ProjectManager> {

    @Override
    public ProjectManager map(ProjectManager manager, Record record) {
        manager.setId(record.getLong("User.id"));
        manager.setFirstName(record.getString("User.firstName"));
        manager.setLastName(record.getString("User.lastName"));
        manager.setPassword(record.getString("User.password"));
        manager.setLogin(record.getString("User.login"));

        manager.setPosition(Employee.Position
                .valueOf(record.getString("Employee.position")));
        return manager;
    }
}
