package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 07.02.2017.
 */
public class ProjectMapper implements RecordMapper<Project> {

    @Override
    public Project map(Project project, Record record) {
        project.setId(record.getLong("id"));
        project.setName(record.getString("name"));
        project.setStartDate(record.getDate("startDate"));
        project.setEndDate(record.getDate("endDate"));
        project.setPlanedStartDate(record.getDate("planedStartDate"));
        project.setPlanedEndDate(record.getDate("planedEndDate"));

        return project;
    }
}
