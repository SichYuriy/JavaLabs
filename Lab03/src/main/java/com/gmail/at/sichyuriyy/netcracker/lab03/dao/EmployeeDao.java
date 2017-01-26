package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface EmployeeDao extends AbstractDao<Long, Employee> {


    List<Employee> findByTaskId(Long id);
    Employee findByTimeRequestId(Long id);
    Employee findByTaskConfirmationId(Long id);
}
