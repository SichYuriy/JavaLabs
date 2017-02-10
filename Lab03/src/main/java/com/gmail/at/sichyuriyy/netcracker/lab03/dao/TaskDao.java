package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Task;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface TaskDao extends AbstractDao<Long, Task> {

    List<Task> findByEmployeeId(Long id);
    List<Task> findBySprintId(Long id);
    List<Task> findChildTasksByParentTaskId(Long id);
    List<Task> findDependenciesByParentTaskId(Long id);
    Task findByTimeRequestId(Long id);
    void updateEmployees(Long taskId, List<Employee> employees);
    void addEmployee(Long taskId, Employee employee);
    void addEmployees(Long taskId, List<Employee> employees);
    void deleteEmployee(Long taskId, Employee employee);
    void deleteAllEmployees(Long taskId);
    Task findByTaskConfirmationId(Long id);
    Task findByChildTaskId(Long id);

    void updateDependencies(Long taskId, List<Task> dependencies);
    void addDependency(Long taskId, Task dependency);
    void addDependencies(Long taskId, List<Task> dependencies);
    void deleteDependency(Long taskId, Task dependency);
    void deleteAllDependencies(Long taskId);

}
