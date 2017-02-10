package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Sprint;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface SprintDao extends AbstractDao<Long, Sprint> {

    List<Sprint> findByProjectId(Long id);
    Sprint findByTaskId(Long id);
    Sprint findByNextSprintId(Long id);
    Sprint findByPreviousSprintId(Long id);

    void updateNextSprint(Long sprintId, Long nextSprintId);
    void deleteNextSprint(Long sprintId);

    void updatePreviousSprint(Long sprintId, Long previousSprintId);
    void deletePreviousSprint(Long sprintId);
}
