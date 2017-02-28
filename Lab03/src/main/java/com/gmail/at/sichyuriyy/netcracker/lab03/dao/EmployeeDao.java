package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface EmployeeDao extends AbstractDao<Long, Employee> {

    Employee findByTaskConfirmationId(Long id);
    List<Employee> findByTaskId(Long id);

}
