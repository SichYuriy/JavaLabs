package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Yuriy on 25.01.2017.
 */
public interface Database {

    void initStorage();

    void createTable(String name, List<Pair<String, DataType>> properties);
    void dropTable(String name);
    List<Pair<String, DataType>> describeTable(String tableName);

    List<Record> selectFrom(String tableName);
    Record selectFrom(String tableName, Long id);
    List<Record> selectFrom(String tableName, String filterName, Object filterValue);
    List<Record> selectFrom(String tableName, List<Pair<String, Object>> filters);
    List<Record> selectFrom(String tableName, String filterName, Predicate<Object> filter);
    List<Record> selectFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter);

    Long insertInto(String tableName, List<Pair<String, Object>> values);

    void deleteFrom(String tableName);
    void deleteFrom(String tableName, Long id);
    void deleteFrom(String tableName, String filterName, Object filterValue);
    void deleteFrom(String tableName, List<Pair<String, Object>> filters);
    void deleteFrom(String tableName, String filterName, Predicate filter);
    void deleteFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter);

    void update(String tableName, Long id, List<Pair<String, Object>> values);
    void update(String tableName, List<Pair<String, Object>> values,
               String filterName, Object filterValue);

    boolean tableExists(String tableName);

}
