package com.gmail.at.sichyuriyy.netcracker.lab03.dao.collections.database;

import java.sql.Date;
import java.util.Iterator;

/**
 * Created by Yuriy on 25.01.2017.
 */
public interface CollectionsResultSet {

    Boolean hasNext();
    void next();

    String getString(String propertyName);
    Long getLong(String propertyName);
    Integer getInteger(String propertyName);
    Double getDouble(String propertyName);
    Date getDate(String propertyName);

}
