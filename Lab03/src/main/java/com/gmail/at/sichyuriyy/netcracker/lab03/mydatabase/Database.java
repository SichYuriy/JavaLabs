package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase;

import javafx.util.Pair;

import java.util.List;

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

   Long insertInto(String tableName, List<Pair<String, Object>> values);

   void deleteFrom(String tableName, Long id);
   void deleteFrom(String tableName, String filterName, Object filterValue);
   void deleteFrom(String tableName, List<Pair<String, Object>> filters);

   void update(String tableName, Long id, List<Pair<String, Object>> values);
   void update(String tableName, List<Pair<String, Object>> values,
               String filterName, Object filterValue);

}
