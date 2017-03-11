package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.util.Pair;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class JsonMetadataParser implements JsonParser<Pair<String, Map<String, DataType>>> {

    public static JsonMetadataParser getParser() {
        return new JsonMetadataParser();
    }

    private JsonMetadataParser() {

    }

    @Override
    public Pair<String, Map<String, DataType>> fromJson(Gson gson, String str)
            throws IOException {

        String tableName;
        Map<String, DataType> properties = new HashMap<>();

        try(JsonReader reader = gson.newJsonReader(new StringReader(str))) {
            reader.beginObject();
            reader.nextName();
            tableName = reader.nextString();

            reader.nextName();
            reader.beginObject();
            while (reader.hasNext()) {
                String propertyName = reader.nextName();
                DataType propertyType = DataType.valueOf(reader.nextString());
                properties.put(propertyName, propertyType);
            }
        }

        return new Pair<>(tableName, properties);
    }
}
