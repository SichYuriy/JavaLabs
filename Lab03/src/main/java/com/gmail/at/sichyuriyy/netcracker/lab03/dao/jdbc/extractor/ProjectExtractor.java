package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectManagerDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.SprintDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Project;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.ProjectProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/22/2017.
 */
public class ProjectExtractor implements EntityExtractor<Project> {
    
    private ProjectManagerDao projectManagerDao;
    private CustomerDao customerDao;
    private SprintDao sprintDao;
    private Map<String, AttributeExtractor<ProjectProxy>> attributeMap;
    
    public ProjectExtractor(ProjectManagerDao projectManagerDao, CustomerDao customerDao, SprintDao sprintDao) {
        this.projectManagerDao = projectManagerDao;
        this.customerDao = customerDao;
        this.sprintDao = sprintDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("name", (obj, rs) -> obj.setName(rs.getString("text_value")));
        attributeMap.put("startDate", (obj, rs) -> obj.setStartDate(rs.getDate("date_value")));
        attributeMap.put("endDate", (obj, rs) -> obj.setEndDate(rs.getDate("date_value")));
        attributeMap.put("plannedStartDate", (obj, rs) -> obj.setPlannedStartDate(rs.getDate("date_value")));
        attributeMap.put("plannedEndDate", (obj, rs) -> obj.setPlannedEndDate(rs.getDate("date_value")));
        attributeMap.put("customerId", (obj, rs) -> obj.setCustomerId(rs.getLong("ref_value")));
        attributeMap.put("managerId", (obj, rs) -> obj.setManagerId(rs.getLong("ref_value")));
    }

    @Override
    public List<Project> extract(ResultSet rs) throws SQLException {
        Map<Long, ProjectProxy> projectMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            ProjectProxy projectProxy = getProxy(projectMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(projectProxy, rs);
        }
        ArrayList<Project> result = new ArrayList<>();

        for (Project project: projectMap.values()) {
            result.add(project);
        }
        return result;
    }

    private ProjectProxy getProxy(Map<Long, ProjectProxy> map, Long id) {
        if (!map.containsKey(id)) {
            ProjectProxy proxy = new ProjectProxy(projectManagerDao, customerDao, sprintDao);
            proxy.setId(id);
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
