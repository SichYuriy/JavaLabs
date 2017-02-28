package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class TaskMapper implements RecordMapper<Task> {
    @Override
    public Task map(Task task, Record record) {
        task.setId(record.getLong("id"));
        task.setEstimateTime(record.getInteger("estimateTime"));
        task.setExecutionTime(record.getInteger("executionTime"));
        task.setName(record.getString("name"));
        task.setRequiredPosition(Employee.EmployeePosition.valueOf(record.getString("requiredPosition")));
        task.setStatus(Task.TaskStatus.valueOf(record.getString("status")));

        return task;
    }
}
