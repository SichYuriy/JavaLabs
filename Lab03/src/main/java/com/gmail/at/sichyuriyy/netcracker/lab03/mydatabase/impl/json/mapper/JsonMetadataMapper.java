package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import javafx.util.Pair;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class JsonMetadataMapper implements JsonMapper<Pair<String, Map<String, DataType>>> {

    public static JsonMetadataMapper getMapper() {
        return new JsonMetadataMapper();
    }

    private JsonMetadataMapper() {

    }
    
    
    @Override
    public String toJson(Gson gson, Pair<String, Map<String, DataType>> table) {
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = new JsonWriter(stringWriter)) {
            writer.beginObject()
                    .name("tableName").value(table.getKey())
                    .name("properties").jsonValue(gson.toJson(table.getValue()))
                    .endObject();
        } catch (IOException e) {
            throw new IllegalArgumentException("json parser is not correct");
        }
        return stringWriter.toString();
    }
}
