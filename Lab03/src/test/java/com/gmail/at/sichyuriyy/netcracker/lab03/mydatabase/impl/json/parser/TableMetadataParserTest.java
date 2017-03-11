package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import javafx.util.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class TableMetadataParserTest {


    private static final String TABLE_JSON1 = "{" +
                "\"tableName\":\"table1\"," +
                "\"properties\":{" +
                    "\"id\":\"LONG\"," +
                    "\"age\":\"INTEGER\"" +
                "}" +
            "}";

    private static final String TABLE_NAME = "table1";

    private Map<String, DataType> tableProps = new HashMap<>();
    {
        tableProps.put("id", DataType.LONG);
        tableProps.put("age", DataType.INTEGER);
    }

    @Test
    public void fromJson() throws Exception {
        JsonMetadataParser parser = JsonMetadataParser.getParser();
        Pair<String, Map<String, DataType>> table = parser.fromJson(new Gson(), TABLE_JSON1);

        assertEquals(TABLE_NAME, table.getKey());
        TestUtils.equalContentCollections(tableProps.entrySet(), table.getValue().entrySet());
    }

}