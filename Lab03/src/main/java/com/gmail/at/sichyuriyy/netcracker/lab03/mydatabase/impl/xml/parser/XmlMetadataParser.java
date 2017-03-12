package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import javafx.util.Pair;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class XmlMetadataParser implements XmlParser<Pair<String, Map<String, DataType>>> {

    public static XmlMetadataParser getParser() {
        return new XmlMetadataParser();
    }

    private XmlMetadataParser() {

    }

    @Override
    public Pair<String, Map<String, DataType>> fromXml(XMLInputFactory factory, String str) throws XMLStreamException {
        String tableName;
        Map<String, DataType> properties = new HashMap<>();

        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(str));
        reader.next();//table
        reader.next();//name
        reader.next();//value
        tableName = reader.getText();
        reader.next();
        reader.next();
        reader.next();
        while (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
            String propertyName = reader.getName().toString();
            reader.next();
            DataType dataType = DataType.valueOf(reader.getText());
            reader.next();
            reader.next();
            properties.put(propertyName, dataType);
        }

        return new Pair<>(tableName, properties);
    }
}
