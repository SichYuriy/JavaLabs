package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

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
        project.setPlannedStartDate(record.getDate("plannedStartDate"));
        project.setPlannedEndDate(record.getDate("plannedEndDate"));

        return project;
    }
}
