package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface EmployeeDao extends AbstractDao<Long, Employee> {

    List<Employee> findByTaskId(Long id);
    void confirmTask(Long employeeId, Task task);

}
