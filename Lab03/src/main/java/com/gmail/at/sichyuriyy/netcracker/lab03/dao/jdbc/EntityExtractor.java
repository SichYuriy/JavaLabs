package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yuriy on 3/17/2017.
 */
@FunctionalInterface
public interface EntityExtractor<T> {

    List<T> extract(ResultSet rs) throws SQLException;
}

