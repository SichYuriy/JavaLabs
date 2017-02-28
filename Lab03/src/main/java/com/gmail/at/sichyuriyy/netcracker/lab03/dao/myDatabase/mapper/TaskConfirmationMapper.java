package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class TaskConfirmationMapper implements RecordMapper<TaskConfirmation> {
    @Override
    public TaskConfirmation map(TaskConfirmation confirmation, Record record) {
        confirmation.setId(record.getLong("id"));
        confirmation.setStatus(TaskConfirmation.ConfirmationStatus.valueOf(record.getString("status")));
        return confirmation;
    }
}
