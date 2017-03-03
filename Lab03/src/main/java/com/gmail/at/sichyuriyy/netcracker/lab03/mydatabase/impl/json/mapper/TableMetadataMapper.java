package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser.TableMetadataParser;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import javafx.util.Pair;
import jdk.nashorn.internal.ir.debug.JSONWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class TableMetadataMapper implements JsonMapper<Pair<String, Map<String, DataType>>> {

    public static TableMetadataMapper getMapper() {
        return new TableMetadataMapper();
    }

    private TableMetadataMapper() {

    }
    
    
    @Override
    public String toJson(Gson gson, Pair<String, Map<String, DataType>> table) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer;
        try {
            writer = gson.newJsonWriter(stringWriter);
            writer.beginObject()
                    .name("tableName").value(table.getKey())
                    .name("properties").jsonValue(gson.toJson(table.getValue()))
                    .endObject()
                    .close();
        } catch (IOException e) {
            throw new IllegalArgumentException("json parser is not correct");
        }
        return stringWriter.toString();
    }
}
