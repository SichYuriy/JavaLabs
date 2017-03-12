package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import javafx.util.Pair;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class XmlMetadataParserTest {

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
    public void fromXml() throws Exception {
        XmlMetadataParser parser = XmlMetadataParser.getParser();

        Pair<String, Map<String, DataType>> table = parser.fromXml(XMLInputFactory.newFactory(), TABLE_XML);
        assertEquals(TABLE_NAME, table.getKey());
        TestUtils.equalContentCollections(tableProps.entrySet(), table.getValue().entrySet());
    }

}