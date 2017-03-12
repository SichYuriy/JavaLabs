package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import javafx.util.Pair;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 06.03.2017.
 */
public class XmlMetadataMapperTest {

    private static final String TABLE_XML = "<table>" +
                "<name>table1</name>" +
                "<properties>" +
                    "<id>LONG</id>" +
                    "<age>INTEGER</age>" +
                "</properties>" +
            "</table>";

    private static final String TABLE_NAME = "table1";

    private Map<String, DataType> tableProps = new HashMap<>();
    {
        tableProps.put("id", DataType.LONG);
        tableProps.put("age", DataType.INTEGER);
    }

    @Test
    public void toXml() {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XmlMetadataMapper mapper = XmlMetadataMapper.getMapper();
        String actual = mapper.toXml(factory, new Pair<>(TABLE_NAME, tableProps));
        assertEquals(TABLE_XML, actual);
    }

}