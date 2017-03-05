package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;

import java.sql.Date;
import java.util.*;

/**
 * Created by Yuriy on 15.02.2017.
 */
public class RecordImpl implements Record {

    private boolean joined;

    private String leftName;
    private String rightName;

    private Map<String, Object> leftProperties;
    private Map<String, Object> rightProperties;

    public RecordImpl(List<Pair<String, Object>> values) {
        leftProperties = new HashMap<>();
        rightProperties = new HashMap<>();
        for (Pair<String, Object> property: values) {
            leftProperties.put(property.getKey(), property.getValue());
        }
    }

    public RecordImpl(Map<String, Object> values) {
        leftProperties = new HashMap<>(values);
        rightProperties = new HashMap<>();
    }

    private RecordImpl() {
        leftProperties = new HashMap<>();
        rightProperties = new HashMap<>();
    }


    @Override
    public String getString(String propertyName) {
        return (String)getObject(propertyName);
    }

    @Override
    public Long getLong(String propertyName) {
        return (Long)getObject(propertyName);
    }

    @Override
    public Integer getInteger(String propertyName) {
        return (Integer)getObject(propertyName);
    }

    @Override
    public Double getDouble(String propertyName) {
        return (Double)getObject(propertyName);
    }

    @Override
    public Date getDate(String propertyName) {
        return (Date)getObject(propertyName);
    }

    @Override
    public Boolean getBoolean(String propertyName) {
        return (Boolean)getObject(propertyName);
    }

    @Override
    public Record join(Record rightRecord, String leftName, String rightName) {
        RecordImpl result = new RecordImpl();
        leftProperties.forEach((str, obj) -> result.leftProperties.put(str, obj));
        rightProperties.forEach((str, obj) -> result.leftProperties.put(str, obj));

        rightRecord.getAll().stream()
                .map((pair) -> new Pair<>(deletePrefix(pair.getKey()), pair.getValue()))
                .forEach((pair) -> result.rightProperties.put(pair.getKey(), pair.getValue()));

        for (Pair<String, Object> pair: rightRecord.getAll()) {
            result.rightProperties.put(pair.getKey(), pair.getValue());
        }
        result.leftName = leftName;
        result.rightName = rightName;
        result.joined = true;
        return result;
    }

    @Override
    public List<Pair<String, Object>> getAll() {
        List<Pair<String, Object>> values = new ArrayList<>();
        if (!joined) {
            leftProperties.forEach((str, obj) -> values.add(new Pair<>(str, obj)));
            rightProperties.forEach((str, obj) -> values.add(new Pair<>(str, obj)));
        } else {
            leftProperties.forEach((str, obj) -> values.add(new Pair<>(leftName + "." + str, obj)));
            rightProperties.forEach((str, obj) -> values.add(new Pair<>(rightName + "." + str, obj)));
        }
        return values;
    }

    private Object getObject(String propertyName) {
        if (!joined) {
            return leftProperties.get(propertyName);
        }
        if (propertyName.indexOf('.') != -1) {
            String prefix = propertyName.substring(0, propertyName.indexOf('.'));
            return getObjectAfterJoin(prefix, deletePrefix(propertyName));
        } else {
            return getObjectAfterJoin(propertyName);
        }
    }

    private Object getObjectAfterJoin(String prefix, String propertyName) {
        Object result = null;
        if (prefix.equals(leftName)) {
            result = leftProperties.get(propertyName);
        } else if(prefix.equals(rightName)) {
            result = rightProperties.get(propertyName);
        }
        return result;
    }

    private String deletePrefix(String propertyName) {
        int prefixLength = propertyName.indexOf('.') + 1;
        return propertyName.substring(prefixLength);
    }

    private Object getObjectAfterJoin(String propertyName) {
        Object result = leftProperties.get(propertyName);
        if (result == null) {
            result = rightProperties.get(propertyName);
        }
        return result;

    }
}
