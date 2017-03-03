package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.binding.IntegerExpression;
import javafx.util.Pair;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Yuriy on 25.01.2017.
 */

public class Main {



    public static void main(String[] args) throws IOException {
        Path path = Paths.get("file_example.txt");
        Files.write(path, Collections.singletonList("line"), StandardOpenOption.APPEND);


    }


}
