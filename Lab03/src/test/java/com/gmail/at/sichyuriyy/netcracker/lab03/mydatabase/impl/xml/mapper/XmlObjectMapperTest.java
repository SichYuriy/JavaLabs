package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper.JsonObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 3/11/2017.
 */
public class XmlObjectMapperTest {

    private final String XML_OBJECT = "<object>" +
            "<date>1</date>" +
            "<boolean>false</boolean>" +
            "<string>str</string>" +
            "<double>1.1</double>" +
            "<integer>11</integer>" +
            "<long>111</long>" +
            "</object>";

    private final String XML_OBJECT_NULL = "<object>" +
            "<date>1</date>" +
            "<boolean>false</boolean>" +
            "<double>1.1</double>" +
            "<long>111</long>" +
            "</object>";

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
    public void toXml() {
        XmlObjectMapper mapper = XmlObjectMapper.getMapper(properties);
        String actual = mapper.toXml(XMLOutputFactory.newFactory(), values);

        assertEquals(XML_OBJECT, actual);
    }

    @Test
    public void toXmlNull() {
        XmlObjectMapper mapper = XmlObjectMapper.getMapper(properties);
        values.put("integer", null);
        values.put("string", null);
        String actual = mapper.toXml(XMLOutputFactory.newFactory(), values);

        assertEquals(XML_OBJECT_NULL, actual);
    }

}