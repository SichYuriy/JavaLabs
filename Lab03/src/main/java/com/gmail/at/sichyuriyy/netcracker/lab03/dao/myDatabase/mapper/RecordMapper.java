package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;

/**
 * Created by Yuriy on 29.01.2017.
 */
public interface RecordMapper<T> {

    T map(T entity, Record record);
}
