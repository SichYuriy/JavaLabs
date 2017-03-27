package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.TaskProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/22/2017.
 */
public class TaskExtractor implements EntityExtractor<Task> {

    private EmployeeDao employeeDao;
    private TaskDao taskDao;
    private TaskConfirmationDao taskConfirmationDao;
    private SprintDao sprintDao;
    private TimeRequestDao timeRequestDao;

    private Map<String, AttributeExtractor<TaskProxy>> attributeMap;


    public TaskExtractor(EmployeeDao employeeDao, TaskDao taskDao,
                         TaskConfirmationDao taskConfirmationDao, SprintDao sprintDao,
                         TimeRequestDao timeRequestDao) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.sprintDao = sprintDao;
        this.timeRequestDao = timeRequestDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("name", (obj, rs) -> obj.setName(rs.getString("text_value")));
        attributeMap.put("estimateTime", (obj, rs) -> obj.setEstimateTime(rs.getInt("number_value")));
        attributeMap.put("executionTime", (obj, rs) -> obj.setExecutionTime(rs.getInt("number_value")));
        attributeMap.put("requiredPosition", (obj, rs) -> obj.setRequiredPosition(Employee.Position.valueOf(rs.getString("text_value"))));
        attributeMap.put("status", (obj, rs) -> obj.setStatus(Task.Status.valueOf(rs.getString("text_value"))));
        attributeMap.put("sprintId", (obj, rs) -> obj.setSprintId(rs.getLong("ref_value")));
        attributeMap.put("parentTaskId", (obj, rs) -> obj.setParentTaskId(rs.getLong("ref_value")));
    }

    @Override
    public List<Task> extract(ResultSet rs) throws SQLException {
        Map<Long, TaskProxy> taskMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            TaskProxy taskProxy = getProxy(taskMap, id);
            String attributeName = rs.getString("attr_name");
            System.out.println(attributeName);
            attributeMap.get(attributeName).set(taskProxy, rs);
        }
        ArrayList<Task> result = new ArrayList<>();

        for (Task task: taskMap.values()) {
            result.add(task);
        }
        return result;
    }

    private TaskProxy getProxy(Map<Long, TaskProxy> map, Long id) {
        if (!map.containsKey(id)) {
            TaskProxy proxy = new TaskProxy(employeeDao, taskDao, taskConfirmationDao, sprintDao, timeRequestDao);
            proxy.setId(id);
            
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
