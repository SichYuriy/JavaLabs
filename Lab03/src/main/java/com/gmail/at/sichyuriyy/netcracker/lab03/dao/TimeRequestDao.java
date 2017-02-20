package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.TimeRequest;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface TimeRequestDao extends AbstractDao<Long, TimeRequest> {

    List<TimeRequest> findByProjectManagerId(Long id);
    List<TimeRequest> findByTaskId(Long id);
    List<TimeRequest> findByEmployeeId(Long id);

}
