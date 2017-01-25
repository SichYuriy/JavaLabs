package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Customer;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface CustomerDao extends AbstractDao<Long, Customer> {

    Customer findByProjectId(Long id);

}
