package com.gmail.at.sichyuriyy.netcracker.lab03.dao.myDatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 06.02.2017.
 */
public class CustomerMapper implements RecordMapper<Customer> {

    @Override
    public Customer map(Customer customer, Record record) {
        customer.setId(record.getLong("User.id"));
        customer.setFirstName(record.getString("User.firstName"));
        customer.setLastName(record.getString("User.lastName"));
        customer.setPassword(record.getString("User.password"));
        customer.setLogin(record.getString("User.login"));

        return customer;
    }
}
