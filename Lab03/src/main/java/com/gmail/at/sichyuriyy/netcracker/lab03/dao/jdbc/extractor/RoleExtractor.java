package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 3/17/2017.
 */
public class RoleExtractor implements EntityExtractor<Role> {

    @Override
    public List<Role> extract(ResultSet rs) throws SQLException {
        List<Role> result = new ArrayList<>();
        while (rs.next()) {
            result.add(Role.valueOf(rs.getString("text_value")));
        }
        return result;
    }
}
