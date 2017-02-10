package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Employee;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.User;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface UserDao {

    User findById(Long id);
    void update(User user);

    List<Role> findRolesByUserId(Long userId);
    void updateRoles(Long userId, List<Role> roles);
    void addRole(Long userId, Role role);
    void addRoles(Long userId, List<Role> roles);
    void deleteRole(Long userId, Role role);
    void deleteAllRoles(Long userId);

}
