package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.ProjectProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.SprintProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/22/2017.
 */
public class SprintExtractor implements EntityExtractor<Sprint> {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private SprintDao sprintDao;
    private Map<String, AttributeExtractor<SprintProxy>> attributeMap;


    public SprintExtractor(ProjectDao projectDao, TaskDao taskDao, SprintDao sprintDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
        this.sprintDao = sprintDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("name", (obj, rs) -> obj.setName(rs.getString("text_value")));
        attributeMap.put("startDate", (obj, rs) -> obj.setStartDate(rs.getDate("date_value")));
        attributeMap.put("endDate", (obj, rs) -> obj.setEndDate(rs.getDate("date_value")));
        attributeMap.put("plannedStartDate", (obj, rs) -> obj.setPlannedStartDate(rs.getDate("date_value")));
        attributeMap.put("plannedEndDate", (obj, rs) -> obj.setPlannedEndDate(rs.getDate("date_value")));
        attributeMap.put("finished", (obj, rs) -> obj.setFinished(rs.getBoolean("number_value")));
        attributeMap.put("nextSprintId", (obj, rs) -> obj.setNextSprintId(rs.getLong("ref_value")));
        attributeMap.put("previousSprintId", (obj, rs) -> obj.setPreviousSprintId(rs.getLong("ref_value")));
        attributeMap.put("projectId", (obj, rs) -> obj.setProjectId(rs.getLong("ref_value")));
    }

    @Override
    public List<Sprint> extract(ResultSet rs) throws SQLException {
        Map<Long, SprintProxy> sprintMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            SprintProxy sprintProxy = getProxy(sprintMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(sprintProxy, rs);
        }
        ArrayList<Sprint> result = new ArrayList<>();

        for (Sprint sprint: sprintMap.values()) {
            result.add(sprint);
        }
        return result;
    }

    private SprintProxy getProxy(Map<Long, SprintProxy> map, Long id) {
        if (!map.containsKey(id)) {
            SprintProxy proxy = new SprintProxy(projectDao, taskDao, sprintDao);
            proxy.setId(id);
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
