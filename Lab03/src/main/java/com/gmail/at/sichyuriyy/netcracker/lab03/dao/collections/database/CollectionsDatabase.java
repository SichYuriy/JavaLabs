package com.gmail.at.sichyuriyy.netcracker.lab03.dao.collections.database;

import javafx.util.Pair;

import java.util.List;

/**
 * Created by Yuriy on 25.01.2017.
 */
public interface CollectionsDatabase {

   void createTable(String name, Pair<String, DataType> properties);
   void dropTable(String name);
   CollectionsResultSet selectFrom(String tableName);
   CollectionsResultSet selectFrom(String tableName, Long id);
   CollectionsResultSet selectFrom(String tableName, List<Pair<String, Object>> filters);



   void insertInto(String tableName, List<Pair<String, Object>> values);
   void deleteFrom(String tableName, Long id);
   void update(String tableName, Long id, List<Pair<String, Object>> values);

   void startTransaction();
   void commit();
   void rollback();

}
