package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

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
public class JsonMetadataMapperTest {

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
    public void toJson() {
        JsonMetadataMapper mapper = JsonMetadataMapper.getMapper();
        String actual = mapper.toJson(new Gson(), new Pair<>(TABLE_NAME, tableProps));
        assertTrue(actual.equals(TABLE_JSON1));
    }

}