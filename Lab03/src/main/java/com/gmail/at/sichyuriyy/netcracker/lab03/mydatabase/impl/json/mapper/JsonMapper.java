package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper;

import com.google.gson.Gson;

/**
 * Created by Yuriy on 02.03.2017.
 */
public interface JsonMapper<T> {

    String toJson(Gson gson, T obj);
}
