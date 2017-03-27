package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.*;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.ProjectManager;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.ProjectManagerProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/22/2017.
 */
public class ProjectManagerExtractor implements EntityExtractor<ProjectManager> {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private TaskConfirmationDao taskConfirmationDao;
    private UserDao userDao;
    private Map<String, AttributeExtractor<ProjectManagerProxy>> attributeMap;


    public ProjectManagerExtractor(ProjectDao projectDao, TaskDao taskDao, TaskConfirmationDao taskConfirmationDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.userDao = userDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("firstName", (obj, rs) -> obj.setFirstName(rs.getString("text_value")));
        attributeMap.put("lastName", (obj, rs) -> obj.setLastName(rs.getString("text_value")));
        attributeMap.put("login", (obj, rs) -> obj.setLogin(rs.getString("text_value")));
        attributeMap.put("password", (obj, rs) -> obj.setPassword(rs.getString("text_value")));
        attributeMap.put("position", (obj, rs) -> obj.setPosition(Employee.Position.valueOf(rs.getString("text_value"))));
    }

    @Override
    public List<ProjectManager> extract(ResultSet rs) throws SQLException {
        Map<Long, ProjectManagerProxy> projectManagerMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            ProjectManagerProxy projectManagerProxy = getProxy(projectManagerMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(projectManagerProxy, rs);
        }
        ArrayList<ProjectManager> result = new ArrayList<>();

        for (ProjectManager projectManager: projectManagerMap.values()) {
            result.add(projectManager);
        }
        return result;
    }

    private ProjectManagerProxy getProxy(Map<Long, ProjectManagerProxy> map, Long id) {
        if (!map.containsKey(id)) {
            ProjectManagerProxy proxy = new ProjectManagerProxy(projectDao, taskDao, taskConfirmationDao, userDao);
            proxy.setId(id);
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
