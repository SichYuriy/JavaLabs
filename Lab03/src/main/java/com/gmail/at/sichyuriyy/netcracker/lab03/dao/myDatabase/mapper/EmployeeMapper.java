package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 06.02.2017.
 */
public class EmployeeMapper implements RecordMapper<Employee> {

    @Override
    public Employee map(Employee employee, Record record) {
        employee.setId(record.getLong("User.id"));
        employee.setFirstName(record.getString("User.firstName"));
        employee.setLastName(record.getString("User.lastName"));
        employee.setPassword(record.getString("User.password"));
        employee.setLogin(record.getString("User.login"));

        employee.setPosition(Employee.EmployeePosition
                .valueOf(record.getString("Employee.position")));
        return employee;
    }
}
