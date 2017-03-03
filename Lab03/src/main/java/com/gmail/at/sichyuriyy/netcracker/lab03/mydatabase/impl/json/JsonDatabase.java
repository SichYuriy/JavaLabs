package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RequestValidator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper.TableMetadataMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser.TableMetadataParser;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;
import com.google.gson.Gson;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Yuriy on 01.03.2017.
 */
public class JsonDatabase implements Database {

    private static final String DB_ROOT = "_dbRoot";
    private static final String METADATA_FILE_NAME = "_metadata.jsondb";
    private static final String TABLES_DIRECTORY = "_tables";

    private final Path root;
    private final Path metadataFilePath;
    private final Path tablesPath;

    private boolean storageInitialized;
    private boolean rewriteOldData;

    private Gson gson = new Gson();

    private Map<String, Map<String, DataType>> metadata;

    private RequestValidator requestValidator;


    public JsonDatabase(String rootDirectoryPath, boolean rewriteOldData) {
        this.root = Paths.get(rootDirectoryPath, DB_ROOT);
        this.metadataFilePath = Paths.get(rootDirectoryPath, DB_ROOT, METADATA_FILE_NAME);
        this.tablesPath = Paths.get(rootDirectoryPath, TABLES_DIRECTORY);
        this.rewriteOldData = rewriteOldData;
    }

    public JsonDatabase(String rootDirectoryPath) {
        this(rootDirectoryPath, false);
    }


    @Override
    public void initStorage() {
        if (!rewriteOldData) {
            readMetadata();
        } else {
            cleanDatabase();
            createDatabaseStructure();
        }
        storageInitialized = true;
        requestValidator = new RequestValidator();
    }

    @Override
    public void createTable(String name, List<Pair<String, DataType>> properties) {
        checkInitialization();
        checkTableNameAbsent(name);
        requestValidator.validateCreateTableRequest(properties);
        Map<String, DataType> propertyMap = new HashMap<>();
        for (Pair<String, DataType> property: properties) {
            propertyMap.put(property.getKey(), property.getValue());
        }
        propertyMap.put("id", DataType.LONG);
        String propertiesJson = TableMetadataMapper.getMapper().toJson(gson, new Pair<>(name, propertyMap));
        try {
            Files.write(metadataFilePath, Collections.singletonList(propertiesJson), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("can not change metadata file", e);
        }
        metadata.put(name, propertyMap);
    }

    @Override
    public void dropTable(String name) {
        checkInitialization();
        checkTableName(name);
        metadata.remove(name);

    }

    @Override
    public List<Pair<String, DataType>> describeTable(String tableName) {
        List<Pair<String, DataType>> result = new ArrayList<>();
        Map<String, DataType> properties = metadata.get(tableName);
        if (properties == null) {
            return result;
        }
        for (String propertyName: properties.keySet()) {
            result.add(new Pair<>(propertyName, properties.get(propertyName)));
        }
        return result;
    }

    @Override
    public List<Record> selectFrom(String tableName) {
        return null;
    }

    @Override
    public Record selectFrom(String tableName, Long id) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Object filterValue) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, List<Pair<String, Object>> filters) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Predicate<Object> filter) {
        return null;
    }

    @Override
    public List<Record> selectFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        return null;
    }

    @Override
    public Long insertInto(String tableName, List<Pair<String, Object>> values) {
        return null;
    }

    @Override
    public void deleteFrom(String tableName) {

    }

    @Override
    public void deleteFrom(String tableName, Long id) {

    }

    @Override
    public void deleteFrom(String tableName, String filterName, Object filterValue) {

    }

    @Override
    public void deleteFrom(String tableName, List<Pair<String, Object>> filters) {

    }

    @Override
    public void deleteFrom(String tableName, String filterName, Predicate filter) {

    }

    @Override
    public void deleteFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {

    }

    @Override
    public void update(String tableName, Long id, List<Pair<String, Object>> values) {

    }

    @Override
    public void update(String tableName, List<Pair<String, Object>> values, String filterName, Object filterValue) {

    }

    @Override
    public boolean tableExists(String tableName) {
        return metadata.containsKey(tableName);
    }

    private void readMetadata() {
        List<String> tableJsonList;
        try {
            tableJsonList = Files.readAllLines(metadataFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not read metadata file", e);
        }
        metadata = new HashMap<>();
        Pair<String, Map<String, DataType>> table;
        for (String propertiesJson: tableJsonList) {
            try {
                table = TableMetadataParser.getParser().fromJson(gson, propertiesJson);
            } catch (IOException e) {
                throw new IllegalStateException("Metadata is corrupted", e);
            }
            metadata.put(table.getKey(), table.getValue());
        }
    }

    private void changeDataValues() {

    }

    private void createDatabaseStructure() {
        try {
            Files.createDirectory(root);
            Files.createDirectory(tablesPath);
            Files.createFile(metadataFilePath);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "can not create database directory-file set", e);
        }
    }

    private void cleanDatabase() {
        try {
            FileUtils.deleteDirRecursively(root);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Can not clean database. Some files are used by other process" +
                            " or protected from deletion", e);
        }
    }

    private void checkInitialization() {
        if (!storageInitialized) {
            throw new IllegalStateException("Storage not initialized(init storage"
                    + "must be called before)");
        }
    }

    private void checkTableNameAbsent(String tableName) {
        if (metadata.containsKey(tableName)) {
            throw new IllegalArgumentException("table " + tableName + " is already exist");
        }
    }

    private void checkTableName(String tableName) {
        if (!metadata.containsKey(tableName)) {
            throw new NoSuchElementException();
        }
    }





}
