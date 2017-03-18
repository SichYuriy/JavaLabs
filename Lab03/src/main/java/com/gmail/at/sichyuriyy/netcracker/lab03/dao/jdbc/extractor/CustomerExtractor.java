package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.EntityExtractor;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy on 3/17/2017.
 */
public class CustomerExtractor implements EntityExtractor<Customer> {

    @Override
    public List<Customer> extract(ResultSet rs) throws SQLException {
        return null;
    }
}
