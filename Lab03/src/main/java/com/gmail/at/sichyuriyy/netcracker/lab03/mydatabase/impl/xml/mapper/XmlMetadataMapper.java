package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import javafx.util.Pair;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Yuriy on 05.03.2017.
 */
public class XmlMetadataMapper implements XmlMapper<Pair<String, Map<String, DataType>>> {

    public static XmlMetadataMapper getMapper() {
        return new XmlMetadataMapper();
    }

    private XmlMetadataMapper() {

    }

    @Override
    public String toXml(XMLOutputFactory factory, Pair<String, Map<String, DataType>> pair) {
        String tableName = pair.getKey();
        Map<String, DataType> properties = pair.getValue();
        StringWriter stringWriter = new StringWriter();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(stringWriter);
            writer.writeStartElement("table");
            writer.writeStartElement("name");
            writer.writeCharacters(tableName);
            writer.writeEndElement();
            writer.writeStartElement("properties");
            for (String key: properties.keySet()) {
                writer.writeStartElement(key);
                writer.writeCharacters(properties.get(key).toString());
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndElement();
            writer.close();
            stringWriter.close();
        } catch (XMLStreamException | IOException e) {
            throw new IllegalStateException("mapper is not correct");
        }
        return stringWriter.toString();
    }
}
