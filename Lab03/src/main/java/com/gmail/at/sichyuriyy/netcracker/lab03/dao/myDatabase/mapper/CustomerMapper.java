package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 06.02.2017.
 */
public class CustomerMapper implements RecordMapper<Customer> {

    @Override
    public Customer map(Customer customer, Record record) {
        customer.setId(record.getLong("id"));
        customer.setFirstName(record.getString("firstName"));
        customer.setLastName(record.getString("lastName"));
        customer.setPassword(record.getString("password"));
        customer.setLogin(record.getString("login"));

        return customer;
    }
}
