package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception.RequestValidationException;
import com.sun.prism.PixelFormat;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 17.02.2017.
 */
public class RequestValidatorTest {

    private RequestValidator requestValidator = new RequestValidator();


    @Test(expected = RequestValidationException.class)
    public void validateIdProperty() throws Exception {
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("id", DataType.LONG));
        requestValidator.validateCreateTableRequest(properties);
    }

    @Test(expected = RequestValidationException.class)
    public void createPropertyDuplication() throws Exception {
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("name", DataType.STRING));
        properties.add(new Pair<>("name", DataType.STRING));
        requestValidator.validateCreateTableRequest(properties);
    }

    @Test(expected = RequestValidationException.class)
    public void createNullDataType() throws Exception {
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("age", null));
        requestValidator.validateCreateTableRequest(properties);
    }

    @Test
    public void validatePropertyType() throws Exception {
        String name = "age";
        Object val = 12L;
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        requestValidator.validatePropertyType(name, val, properties);
    }

    @Test
    public void validatePropertyTypeNull() throws Exception {
        String name = "age";
        Object val = null;
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        requestValidator.validatePropertyType(name, val, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void validatePropertyTypeBadObj() throws Exception {
        String name = "age";
        Object val = "12";
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        requestValidator.validatePropertyType(name, val, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void validatePropertiesType() throws Exception {
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("age", 12L));
        values.add(new Pair<>("name", 12L));
        requestValidator.validatePropertiesType(values, properties);
    }

    @Test
    public void checkProperty() throws Exception {
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);
        requestValidator.checkProperty("name", properties);
    }

    @Test(expected = RequestValidationException.class)
    public void checkPropertyNoExist() throws Exception {
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);
        requestValidator.checkProperty("width", properties);
    }

    @Test
    public void checkProperties() throws Exception {
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);
        List<String> values = new ArrayList<>();
        values.add("age");
        values.add("name");
        requestValidator.checkProperties(values, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void checkPropertiesNoExist() throws Exception {
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);
        List<String> values = new ArrayList<>();
        values.add("age");
        values.add("width");
        requestValidator.checkProperties(values, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void insertId() throws Exception {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("id", 123L));
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);

        requestValidator.validateInsertUpdateRequest(values, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void insertPropertyDuplication() throws Exception {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("name", "name1"));
        values.add(new Pair<>("name", "name2"));
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);

        requestValidator.validateInsertUpdateRequest(values, properties);
    }

    @Test(expected = RequestValidationException.class)
    public void insertBadType() throws Exception {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("name", 12L));
        Map<String, DataType> properties = new HashMap<>();
        properties.put("age", DataType.LONG);
        properties.put("name", DataType.STRING);

        requestValidator.validateInsertUpdateRequest(values, properties);
    }

}