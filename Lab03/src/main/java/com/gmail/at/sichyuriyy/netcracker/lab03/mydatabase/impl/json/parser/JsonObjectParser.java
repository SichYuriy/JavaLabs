package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy on 03.03.2017.
 */
public class JsonObjectParser implements JsonParser<Map<String, Object>> {

    private final Map<DataType, PropertyReader> dataTypeReadMap;

    private Map<String, DataType> properties;

    public static JsonObjectParser getParser(Map<String, DataType> properties) {
        return new JsonObjectParser(properties);
    }


    private JsonObjectParser(Map<String, DataType> properties) {
        this.properties = properties;
        dataTypeReadMap = new HashMap<>();
        dataTypeReadMap.put(DataType.BOOLEAN, JsonReader::nextBoolean);
        dataTypeReadMap.put(DataType.LONG, JsonReader::nextLong);
        dataTypeReadMap.put(DataType.INTEGER, JsonReader::nextInt);
        dataTypeReadMap.put(DataType.DOUBLE, JsonReader::nextDouble);
        dataTypeReadMap.put(DataType.STRING, JsonReader::nextString);
        dataTypeReadMap.put(DataType.DATE, reader -> {
            Long time = reader.nextLong();
            return new Date(time);
        });
    }

    @Override
    public Map<String, Object> fromJson(Gson gson, String str) throws IOException {
        Map<String, Object> result = new HashMap<>();
        prepareValues(result);
        try (JsonReader reader = new JsonReader(new StringReader(str))) {
            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                Object value = dataTypeReadMap.get(properties.get(name)).read(reader);
                result.put(name, value);
            }
        }
        return result;
    }

    private void prepareValues(Map<String, Object> values) {
        for (String propertyName: properties.keySet()) {
            values.put(propertyName, null);
        }
    }

    @FunctionalInterface
    private interface PropertyReader {
        Object read(JsonReader reader) throws IOException;
    }
}
