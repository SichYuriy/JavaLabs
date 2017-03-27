package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.UserDao;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.proxy.CustomerProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 3/17/2017.
 */
public class CustomerExtractor implements EntityExtractor<Customer> {

    private ProjectDao projectDao;
    private UserDao userDao;

    private Map<String, AttributeExtractor<CustomerProxy>> attributeMap;

    public CustomerExtractor(ProjectDao projectDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;

        attributeMap = new HashMap<>();
        attributeMap.put("id", (obj, rs) -> obj.setId(rs.getLong("id")));
        attributeMap.put("firstName", (obj, rs) -> obj.setFirstName(rs.getString("text_value")));
        attributeMap.put("lastName", (obj, rs) -> obj.setLastName(rs.getString("text_value")));
        attributeMap.put("login", (obj, rs) -> obj.setLogin(rs.getString("text_value")));
        attributeMap.put("password", (obj, rs) -> obj.setPassword(rs.getString("text_value")));

    }

    @Override
    public List<Customer> extract(ResultSet rs) throws SQLException {
        Map<Long, CustomerProxy> customerMap = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            CustomerProxy customerProxy = getProxy(customerMap, id);
            String attributeName = rs.getString("attr_name");
            attributeMap.get(attributeName).set(customerProxy, rs);
        }
        ArrayList<Customer> result = new ArrayList<>();

        for (Customer customer: customerMap.values()) {
            result.add(customer);
        }
        return result;
    }

    private CustomerProxy getProxy(Map<Long, CustomerProxy> map, Long id) {
        if (!map.containsKey(id)) {
            CustomerProxy proxy = new CustomerProxy(projectDao, userDao);
            proxy.setId(id);
            map.put(id, proxy);
        }
        return map.get(id);
    }
}
