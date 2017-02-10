package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class Table {

    private Map<String, DataType> properties;
    private Map<Long, Map<String, Object>> rows;

    private Long nextId = 1l;

    public Table(List<Pair<String, DataType>> propertyList) {
        properties = new HashMap<>();
        rows = new HashMap<>();
        propertyList.forEach((pair) -> properties.put(pair.getKey(), pair.getValue()));
    }

    public Map<String, DataType> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, DataType> properties) {
        this.properties = properties;
    }

    public Map<Long, Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(Map<Long, Map<String, Object>> rows) {
        this.rows = rows;
    }

    public Long getNextId() {
        return nextId;
    }

    public void generateNextId() {
        nextId++;
    }
}
