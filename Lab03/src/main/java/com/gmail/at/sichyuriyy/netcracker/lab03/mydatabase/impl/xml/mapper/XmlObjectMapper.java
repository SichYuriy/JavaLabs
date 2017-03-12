package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Yuriy on 3/11/2017.
 */
public class XmlObjectMapper implements XmlMapper<Map<String, Object>> {

    private Set<String> dataValues;

    private XmlObjectMapper(Map<String, DataType> properties) {
        dataValues = new HashSet<>();
        for (String key: properties.keySet()) {
            if (properties.get(key).equals(DataType.DATE)) {
                dataValues.add(key);
            }
        }
    }

    public static XmlObjectMapper getMapper(Map<String, DataType> properties) {
        return new XmlObjectMapper(properties);
    }

    @Override
    public String toXml(XMLOutputFactory factory, Map<String, Object> obj) {
        Map<String, Object> copy = new HashMap<>(obj);
        for (String dataProperty: dataValues) {
            if (copy.get(dataProperty) != null) {
                Long longData = ((Date)copy.get(dataProperty)).getTime();
                copy.put(dataProperty, longData);
            }
        }
        StringWriter stringWriter = new StringWriter();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(stringWriter);
            writer.writeStartElement("object");
            for (String propertyName: copy.keySet()) {
                Object value = copy.get(propertyName);
                if (value != null) {
                    writer.writeStartElement(propertyName);
                    writer.writeCharacters(value.toString());
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
            writer.close();
            stringWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IllegalStateException("mapper is not correct", e);
        }
        return stringWriter.toString();
    }
}
