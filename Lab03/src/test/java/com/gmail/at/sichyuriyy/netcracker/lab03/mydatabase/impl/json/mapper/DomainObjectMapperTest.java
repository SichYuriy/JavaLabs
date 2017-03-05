package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 03.03.2017.
 */
public class DomainObjectMapperTest {

    private final Map<String, DataType> properties;

    private final Map<String, Object> values;
    private final String JSON_OBJECT = "{" +
            "\"date\":1," +
            "\"boolean\":false," +
            "\"string\":\"str\"," +
            "\"double\":1.1," +
            "\"integer\":11," +
            "\"long\":111" +
            "}";

    public DomainObjectMapperTest() {
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
        DomainObjectMapper mapper = DomainObjectMapper.getMapper(properties);
        String actual = mapper.toJson(new Gson(), values);
        assertEquals(JSON_OBJECT, actual);
    }

}