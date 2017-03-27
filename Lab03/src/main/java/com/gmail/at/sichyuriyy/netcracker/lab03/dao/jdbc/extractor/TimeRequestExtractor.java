package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TaskProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TimeRequestProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/23/2017.
 */
public class TimeRequestExtractor implements EntityExtractor<TimeRequest> {

    private TaskDao taskDao;

    private Map<String, AttributeExtractor<TimeRequestProxy>> attributeMap;


    public TimeRequestExtractor(TaskDao taskDao) {
        this.taskDao = taskDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("reason", (obj, rs) -> obj.setReason(rs.getString("text_value")));
        attributeMap.put("requestTime", (obj, rs) -> obj.setRequestTime(rs.getInt("number_value")));
        attributeMap.put("responseTime", (obj, rs) -> obj.setResponseTime(rs.getInt("number_value")));
        attributeMap.put("status", (obj, rs) -> obj.setStatus(TimeRequest.Status.valueOf(rs.getString("text_value"))));
        attributeMap.put("taskId", (obj, rs) -> obj.setTaskId(rs.getLong("ref_value")));
    }

    @Override
    public List<TimeRequest> extract(ResultSet rs) throws SQLException {
        Map<Long, TimeRequestProxy> timeRequestMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            TimeRequestProxy timeRequestProxy = getProxy(timeRequestMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(timeRequestProxy, rs);
        }
        ArrayList<TimeRequest> result = new ArrayList<>();

        for (TimeRequest timeRequest: timeRequestMap.values()) {
            result.add(timeRequest);
        }
        return result;
    }

    private TimeRequestProxy getProxy(Map<Long, TimeRequestProxy> map, Long id) {
        if (!map.containsKey(id)) {
            TimeRequestProxy proxy = new TimeRequestProxy(taskDao);
            proxy.setId(id);

            map.put(id, proxy);
        }
        return map.get(id);
    }
}
