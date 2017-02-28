package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 09.02.2017.
 */
public class SprintMapper implements RecordMapper<Sprint> {

    @Override
    public Sprint map(Sprint sprint, Record record) {
        sprint.setName(record.getString("name"));
        sprint.setStartDate(record.getDate("startDate"));
        sprint.setEndDate(record.getDate("endDate"));
        sprint.setPlannedStartDate(record.getDate("plannedStartDate"));
        sprint.setPlannedEndDate(record.getDate("plannedEndDate"));
        sprint.setFinished(record.getBoolean("finished"));
        sprint.setId(record.getLong("id"));

        return sprint;
    }
}
