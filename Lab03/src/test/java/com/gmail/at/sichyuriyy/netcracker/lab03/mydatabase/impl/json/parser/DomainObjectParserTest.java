package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 05.03.2017.
 */
public class DomainObjectParserTest {

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

    public DomainObjectParserTest() {
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
    public void fromJson() throws IOException {
        DomainObjectParser parser = DomainObjectParser.getParser(properties);
        Map<String, Object> actual = parser.fromJson(new Gson(), JSON_OBJECT);

        assertTrue(TestUtils.equalContentCollections(
                values.entrySet(),
                actual.entrySet()
        ));
    }

}