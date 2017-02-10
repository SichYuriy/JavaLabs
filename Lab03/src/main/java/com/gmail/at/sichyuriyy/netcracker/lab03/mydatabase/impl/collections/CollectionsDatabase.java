package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception.RequestValidationException;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RequestValidator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class CollectionsDatabase implements Database {

    private boolean storageInitialized;

    private Map<String, Table> tables;
    private RequestValidator requestValidator;

    @Override
    public void initStorage() {
        storageInitialized = true;
        tables = new HashMap<>();
        requestValidator = new RequestValidator();
    }

    @Override
    public void createTable(String name, List<Pair<String, DataType>> properties) {
        if (!storageInitialized) {
            throw new IllegalStateException("storage not initialized(init storage" +
                    "must be called before)");
        }
        if (tables.containsKey(name)) {
            throw new IllegalArgumentException("table " + name + " is already exist");
        }
        try {
            requestValidator.validateCreateTableRequest(properties);
            properties.add(new Pair<>("id", DataType.LONG));
            tables.put(name, new Table(properties));
        } catch (RequestValidationException e) {
            throw new IllegalArgumentException(e);
            //TODO: logger
        }

    }

    @Override
    public void dropTable(String name) {
        if (!tables.containsKey(name)) {
            throw new IllegalArgumentException("table" + name + " does not exist");
        }
        tables.remove(name);
    }

    @Override
    public List<Pair<String, DataType>> describeTable(String tableName) {
        List<Pair<String, DataType>> result = new ArrayList<>();
        Table table = tables.get(tableName);
        if (table == null) {
            return result;
        }
        Map<String, DataType> properties = table.getProperties();
        for (String propertyName: properties.keySet()) {
            result.add(new Pair<>(propertyName, properties.get(propertyName)));
        }
        return result;
    }

    @Override
    public List<Record> selectFrom(String tableName) {
        Object obj = new String("123");
        Long l = (Long)obj;
        return null;
    }

    @Override
    public Record selectFrom(String tableName, Long id) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Object filterValue) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, List<Pair<String, Object>> filters) {
        return null;
    }

    @Override
    public Long insertInto(String tableName, List<Pair<String, Object>> values) {
        return null;
    }

    @Override
    public void deleteFrom(String tableName, Long id) {

    }

    @Override
    public void deleteFrom(String tableName, String filterName, Object filterValue) {

    }

    @Override
    public void deleteFrom(String tableName, List<Pair<String, Object>> filters) {

    }

    @Override
    public void update(String tableName, Long id, List<Pair<String, Object>> values) {

    }

    @Override
    public void update(String tableName, List<Pair<String, Object>> values, String filterName, Object filterValue) {

    }
}
