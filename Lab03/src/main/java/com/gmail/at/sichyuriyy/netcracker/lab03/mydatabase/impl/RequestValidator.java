package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception.RequestValidationException;
import javafx.util.Pair;

import java.sql.Date;
import java.util.*;


/**
 * Created by Yuriy on 11.02.2017.
 */
public class RequestValidator {

    private Map<DataType, Class> dataTypeClassMap = new HashMap<DataType, Class>(){{
        put(DataType.BOOLEAN, Boolean.class);
        put(DataType.DATE, Date.class);
        put(DataType.DOUBLE, Double.class);
        put(DataType.INTEGER, Integer.class);
        put(DataType.LONG, Long.class);
        put(DataType.STRING, String.class);
    }};

    public void validateCreateTableRequest(List<Pair<String, DataType>> properties) {
        Set<String> propertySet = new HashSet<>();
        for (Pair<String, DataType> pair: properties) {
            String propertyName = pair.getKey();
            DataType propertyType = pair.getValue();
            if (propertyName.equals("id")) {
                throw new RequestValidationException("id property is auto-generated");
            }
            if (propertySet.contains(propertyName)) {
                throw new RequestValidationException("property " + propertyName +
                        " is already declared");
            }
            if (propertyType == null) {
                throw new RequestValidationException("property type of " + propertyName +
                        " must be defined(not null)");
            }
            propertySet.add(propertyName);
        }
    }

    public void validatePropertyType(String key, Object val,
                                      Map<String, DataType> properties) {

        checkProperty(key, properties);
        if (val == null) {
            return;
        }
        DataType dataType = properties.get(key);
        if (!dataTypeClassMap.get(dataType).isInstance(val)) {
            throw new RequestValidationException("Expected type of " + key
                    + " is " + dataType);
        }
    }

    public void validatePropertiesType(List<Pair<String, Object>> values,
                                     Map<String, DataType> properties) {
        for (Pair<String, Object> pair: values) {
            validatePropertyType(pair.getKey(), pair.getValue(), properties);
        }
    }

    public void checkProperty(String propertyName, Map<String, DataType> properties) {
        if (!properties.containsKey(propertyName)) {
            throw new RequestValidationException("There is no property " + propertyName
                    + " int this table");
        }
    }

    public void checkProperties(List<String> propertyNameList, Map<String, DataType> properties) {
        propertyNameList.forEach((pr) -> checkProperty(pr, properties));
    }

    public void validateInsertUpdateRequest(List<Pair<String, Object>> values,
                                      Map<String, DataType> properties) {
        Set<String> propertySet = new HashSet<>();
        for (Pair<String, Object> pair: values) {
            String propertyName = pair.getKey();
            Object value = pair.getValue();
            if (propertyName.equals("id")) {
                throw new RequestValidationException("id property is auto-generated");
            }
            if (propertySet.contains(propertyName)) {
                throw new RequestValidationException("property " + propertyName +
                        " is already declared");
            }
            validatePropertyType(propertyName, value, properties);
            propertySet.add(propertyName);
        }
    }

}
