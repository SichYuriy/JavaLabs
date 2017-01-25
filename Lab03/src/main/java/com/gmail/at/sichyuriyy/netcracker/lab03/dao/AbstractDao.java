package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import java.util.List;

/**
 * Created by Yuriy on 22.01.2017.
 */
public interface AbstractDao<PK, T> {

    void create(T obj);
    T findById(PK id);
    List<T> findAll();
    void update(T obj);
    void delete(PK id);
}
