package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class XmlObjectParser implements XmlParser<Map<String, Object>> {

    private final Map<DataType, PropertyReader> dataTypeReadMap;

    private Map<String, DataType> properties;

    public static XmlObjectParser getParser(Map<String, DataType> properties) {
        return new XmlObjectParser(properties);
    }


    private XmlObjectParser(Map<String, DataType> properties) {
        this.properties = properties;
        dataTypeReadMap = new HashMap<>();
        dataTypeReadMap.put(DataType.BOOLEAN, Boolean::valueOf);
        dataTypeReadMap.put(DataType.LONG, Long::valueOf);
        dataTypeReadMap.put(DataType.INTEGER, Integer::valueOf);
        dataTypeReadMap.put(DataType.DOUBLE, Double::valueOf);
        dataTypeReadMap.put(DataType.STRING, str -> str);
        dataTypeReadMap.put(DataType.DATE, str -> {
            Long time = Long.valueOf(str);
            return new Date(time);
        });
    }

    @Override
    public Map<String, Object> fromXml(XMLInputFactory factory, String str) throws XMLStreamException {
        Map<String, Object> result = new HashMap<>();
        prepareValues(result);
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(str));
        reader.next();//name object
        reader.next();
        while (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
            String propertyName = reader.getName().toString();
            reader.next();
            String strValue = reader.getText();
            result.put(propertyName, dataTypeReadMap.get(properties.get(propertyName)).read(strValue));
            reader.next();
            reader.next();
        }
        return result;
    }

    private void prepareValues(Map<String, Object> values) {
        for (String propertyName: properties.keySet()) {
            values.put(propertyName, null);
        }
    }

    @FunctionalInterface
    private interface PropertyReader {
        Object read(String value);
    }
}
