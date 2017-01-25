package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.User;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface UserDao extends AbstractDao<Long, User> {

    List<Role> findRolesByUserId(Long userId);

}
