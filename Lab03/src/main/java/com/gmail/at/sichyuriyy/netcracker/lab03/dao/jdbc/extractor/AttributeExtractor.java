package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Yuriy on 3/19/2017.
 */
@FunctionalInterface
public interface AttributeExtractor<T> {
    void set(T obj, ResultSet rs) throws SQLException;
}
