package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Yuriy on 02.03.2017.
 */
public interface JsonParser<T> {

    T fromJson(Gson gson, String str) throws IOException;
}
