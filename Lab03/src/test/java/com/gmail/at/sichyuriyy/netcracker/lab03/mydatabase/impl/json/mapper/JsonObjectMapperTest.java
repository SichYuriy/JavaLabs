package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 03.03.2017.
 */
public class JsonObjectMapperTest {


    private final String JSON_OBJECT = "{" +
            "\"date\":1," +
            "\"boolean\":false," +
            "\"string\":\"str\"," +
            "\"double\":1.1," +
            "\"integer\":11," +
            "\"long\":111" +
            "}";

    private final String JSON_OBJECT_NULL = "{" +
            "\"date\":1," +
            "\"boolean\":false," +
            "\"double\":1.1," +
            "\"long\":111" +
            "}";

    private Map<String, DataType> properties;

    private Map<String, Object> values;

    @Before
    public void setUp() throws Exception {
        properties = new HashMap<>();
        properties.put("long", DataType.LONG);
        properties.put("integer", DataType.INTEGER);
        properties.put("string", DataType.STRING);
        properties.put("date", DataType.DATE);
        properties.put("boolean", DataType.BOOLEAN);
        properties.put("double", DataType.DOUBLE);

        values = new HashMap<>();
        values.put("long", 111L);
        values.put("integer", 11);
        values.put("string", "str");
        values.put("date", new Date(1));
        values.put("boolean", false);
        values.put("double", 1.1d);

    }

    @Test
    public void toJson() {
        JsonObjectMapper mapper = JsonObjectMapper.getMapper(properties);
        String actual = mapper.toJson(new Gson(), values);
        assertEquals(JSON_OBJECT, actual);
    }

    @Test
    public void toJsonNull() {
        JsonObjectMapper mapper = JsonObjectMapper.getMapper(properties);
        values.put("integer", null);
        values.put("string", null);
        String actual = mapper.toJson(new Gson(), values);

        assertEquals(JSON_OBJECT_NULL, actual);
    }

}