package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface SprintDao extends AbstractDao<Long, Sprint> {

    List<Sprint> findByProjectId(Long id);
    Sprint findByTaskId(Long id);
}
