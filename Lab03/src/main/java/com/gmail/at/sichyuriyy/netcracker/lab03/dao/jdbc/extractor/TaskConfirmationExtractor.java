package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TaskConfirmation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/22/2017.
 */
public class TaskConfirmationExtractor implements EntityExtractor<TaskConfirmation> {

    private Map<String, AttributeExtractor<TaskConfirmation>> attributeMap;

    public TaskConfirmationExtractor() {
        attributeMap = new HashMap<>();
        attributeMap.put("status", (obj, rs) ->
                obj.setStatus(TaskConfirmation.Status.valueOf(rs.getString("text_value"))));
        attributeMap.put("taskId", (obj, rs) -> {});
        attributeMap.put("employeeId", (obj, rs) -> {});
    }

    @Override
    public List<TaskConfirmation> extract(ResultSet rs) throws SQLException {
        Map<Long, TaskConfirmation> taskConfirmationMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            TaskConfirmation taskConfirmation = new TaskConfirmation();
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(taskConfirmation, rs);
        }
        ArrayList<TaskConfirmation> result = new ArrayList<>();

        for (TaskConfirmation taskConfirmation: taskConfirmationMap.values()) {
            result.add(taskConfirmation);
        }
        return result;
    }
}
