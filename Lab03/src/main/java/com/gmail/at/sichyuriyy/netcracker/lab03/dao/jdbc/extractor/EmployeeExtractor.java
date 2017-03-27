package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskConfirmationDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TimeRequestDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.CustomerProxy;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.EmployeeProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/21/2017.
 */
public class EmployeeExtractor implements EntityExtractor<Employee> {

    private TaskDao taskDao;
    private TaskConfirmationDao taskConfirmationDao;
    private TimeRequestDao timeRequestDao;
    private UserDao userDao;

    private Map<String, AttributeExtractor<EmployeeProxy>> attributeMap;

    public EmployeeExtractor(TaskDao taskDao, TaskConfirmationDao taskConfirmationDao, TimeRequestDao timeRequestDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.taskConfirmationDao = taskConfirmationDao;
        this.timeRequestDao = timeRequestDao;
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
    public List<Employee> extract(ResultSet rs) throws SQLException {
        Map<Long, EmployeeProxy> employeeMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            EmployeeProxy employeeProxy = getProxy(employeeMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(employeeProxy, rs);
        }
        ArrayList<Employee> result = new ArrayList<>();

        for (Employee employee: employeeMap.values()) {
            result.add(employee);
        }
        return result;
    }

    private EmployeeProxy getProxy(Map<Long, EmployeeProxy> map, Long id) {
        if (!map.containsKey(id)) {
            EmployeeProxy proxy = new EmployeeProxy(taskDao, timeRequestDao, taskConfirmationDao, userDao);
            proxy.setId(id);
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
