package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;


import java.util.*;
import java.sql.Date;

/**
 * Created by Yuriy on 03.03.2017.
 */
public class DomainObjectMapper implements JsonMapper<Map<String, Object>> {

    private Set<String> dataValues;

    private DomainObjectMapper(Map<String, DataType> properties) {
        dataValues = new HashSet<>();
        for (String key: properties.keySet()) {
            if (properties.get(key).equals(DataType.DATE)) {
                dataValues.add(key);
            }
        }
    }

    public static DomainObjectMapper getMapper(Map<String, DataType> properties) {
        return new DomainObjectMapper(properties);
    }

    @Override
    public String toJson(Gson gson, Map<String, Object> obj) {
        Map<String, Object> copy = new HashMap<>(obj);
        for (String dataProperty: dataValues) {
            Long longData = ((Date)copy.get(dataProperty)).getTime();
            copy.put(dataProperty, longData);
        }
        return gson.toJson(copy);
    }


}
