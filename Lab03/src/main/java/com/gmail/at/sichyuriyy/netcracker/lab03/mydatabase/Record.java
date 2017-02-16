package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase;

import javafx.util.Pair;

import java.sql.Date;
import java.util.List;

/**
 * Created by Yuriy on 25.01.2017.
 */
public interface Record {

    String getString(String propertyName);
    Long getLong(String propertyName);
    Integer getInteger(String propertyName);
    Double getDouble(String propertyName);
    Date getDate(String propertyName);
    Boolean getBoolean(String propertyName);

    Record join(Record rightRecord, String leftName, String rightName);

    List<Pair<String, Object>> getAll();

}
