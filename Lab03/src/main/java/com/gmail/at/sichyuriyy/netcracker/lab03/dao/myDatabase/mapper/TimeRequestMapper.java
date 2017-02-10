package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class TimeRequestMapper implements RecordMapper<TimeRequest> {

    @Override
    public TimeRequest map(TimeRequest timeRequest, Record record) {
        timeRequest.setId(record.getLong("id"));
        timeRequest.setReason(record.getString("reason"));
        timeRequest.setRequestTime(record.getInteger("requestTime"));
        timeRequest.setResponseTime(record.getInteger("responseTime"));
        timeRequest.setStatus(TimeRequest.Status.valueOf(record.getString("status")));

        return timeRequest;
    }
}
