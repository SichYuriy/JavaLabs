package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RecordImpl;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RequestValidator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper.JsonObjectMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.mapper.JsonMetadataMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser.JsonObjectParser;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.parser.JsonMetadataParser;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;
import com.google.gson.Gson;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 01.03.2017.
 */
public class JsonDatabase implements Database {

    private static final String DB_ROOT = "_dbRoot";
    private static final String METADATA_FILE_NAME = "_metadata.jsondb";
    private static final String TABLES_DIRECTORY = "_tables";
    private static final String SEQUENCE_FILE_NAME = "_sequence.jsondb";

    private static final int tableFileCount = 5;

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
        this.tablesPath = Paths.get(rootDirectoryPath, DB_ROOT, TABLES_DIRECTORY);
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
            metadata = new HashMap<>();
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
        String propertiesJson = JsonMetadataMapper.getMapper().toJson(gson, new Pair<>(name, propertyMap));
        try {
            Files.write(metadataFilePath, Collections.singletonList(propertiesJson), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("can not change metadata file", e);
        }
        createTableFiles(name);
        metadata.put(name, propertyMap);
    }

    @Override
    public void dropTable(String name) {
        JsonMetadataMapper mapper = JsonMetadataMapper.getMapper();
        checkInitialization();
        checkTableName(name);
        metadata.remove(name);
        List<String> metadataLines = new ArrayList<>();
        for (String tableName: metadata.keySet()) {
            metadataLines.add(mapper.toJson(gson, new Pair<>(tableName, metadata.get(tableName))));
        }

        try {
            Files.write(metadataFilePath, metadataLines);
        } catch (IOException e) {
            throw new IllegalStateException("Can not rewrite metadata", e);
        }
        deleteTableFiles(name);
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
        checkInitialization();
        checkTableName(tableName);
        List<Map<String, Object>> objects = readAllObjects(tableName);
        return objects.stream()
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public Record selectFrom(String tableName, Long id) {
        checkInitialization();
        checkTableName(tableName);
        Map<String, Object> object = readById(tableName, id);

        return (object != null) ? new RecordImpl(object) : null;
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertyType(filterName, filterValue,
                metadata.get(tableName));
        List<Map<String, Object>> objects = readAllObjects(tableName);
        return objects.stream()
                .filter(rowMap -> Objects.equals(rowMap.get(filterName), filterValue))
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> selectFrom(String tableName, List<Pair<String, Object>> filters) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertiesType(filters, metadata.get(tableName));
        List<Map<String, Object>> objects = readAllObjects(tableName);
        return objects.stream()
                .filter(rowMap -> checkRow(rowMap, filters))
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Predicate<Object> filter) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.checkProperty(filterName, metadata.get(tableName));
        List<Map<String, Object>> objects = readAllObjects(tableName);
        return objects.stream()
                .filter((rowMap) -> filter.test(rowMap.get(filterName)))
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> selectFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.checkProperties(filterNames, metadata.get(tableName));
        List<Map<String, Object>> objects = readAllObjects(tableName);
        return objects.stream()
                .filter(filter)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public Long insertInto(String tableName, List<Pair<String, Object>> values) {
        checkInitialization();
        checkTableName(tableName);
        Map<String, DataType> properties = metadata.get(tableName);
        requestValidator.validateInsertUpdateRequest(values, properties);
        Map<String, Object> insertRow = new HashMap<>();
        properties.keySet().forEach(propertyName -> insertRow.put(propertyName, null));
        updateRow(insertRow, values);
        Long id = generateNextId(tableName);
        insertRow.put("id", id);
        String jsonRow = JsonObjectMapper.getMapper(properties).toJson(gson, insertRow);
        Path fileTable = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        try {
            Files.write(fileTable, Collections.singletonList(jsonRow), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("can not add new data to the file", e);
        }
        return id;
    }

    @Override
    public void deleteFrom(String tableName) {
        checkInitialization();
        checkTableName(tableName);
        for (String fileName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, fileName);
            try {
                Files.write(filePath, Collections.emptyList());
            } catch (IOException e) {
                throw new IllegalStateException("can not clean data file", e);
            }
        }
    }

    @Override
    public void deleteFrom(String tableName, Long id) {
        checkInitialization();
        checkTableName(tableName);
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                "_temp_" + getTablePartName(tableName, id));
        deleteContentFromDataFile(filePath, tempFilePath, parser,
                map -> map.get("id").equals(id));
    }

    @Override
    public void deleteFrom(String tableName, String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertyType(filterName, filterValue,
                metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath, parser,
                    map -> Objects.equals(map.get(filterName), filterValue));
        }

    }

    @Override
    public void deleteFrom(String tableName, List<Pair<String, Object>> filters) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertiesType(filters, metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath, parser,
                    map -> {
                        for (Pair<String, Object> filterPair: filters) {
                            if (!Objects.equals(map.get(filterPair.getKey()), filterPair.getValue())) {
                                return false;
                            }
                        }
                        return true;
                    });
        }
    }

    @Override
    public void deleteFrom(String tableName, String filterName, Predicate filter) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.checkProperty(filterName, metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath, parser,
                    map -> filter.test(map.get(filterName)));
        }
    }

    @Override
    public void deleteFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.checkProperties(filterNames, metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath, parser, filter);
        }
    }

    @Override
    public void update(String tableName, Long id, List<Pair<String, Object>> values) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validateInsertUpdateRequest(values, metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        JsonObjectMapper mapper = JsonObjectMapper.getMapper(metadata.get(tableName));
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                "_temp_" + getTablePartName(tableName, id));
        updateContentOfDataFile(filePath, tempFilePath, parser, mapper,
                map -> map.get("id").equals(id),
                values);
    }

    @Override
    public void update(String tableName, List<Pair<String, Object>> values, String filterName, Object filterValue) {
        checkTableName(tableName);
        requestValidator.validateInsertUpdateRequest(values, metadata.get(tableName));
        requestValidator.validatePropertyType(filterName, filterValue,
                metadata.get(tableName));
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        JsonObjectMapper mapper = JsonObjectMapper.getMapper(metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            updateContentOfDataFile(filePath, tempFilePath, parser, mapper,
                    map -> Objects.equals(map.get(filterName), filterValue),
                    values);
        }
    }

    @Override
    public boolean tableExists(String tableName) {
        return metadata.containsKey(tableName);
    }

    private boolean checkRow(Map<String, Object> rowMap,
                             List<Pair<String, Object>> filters) {
        return filters.stream()
                .allMatch(pair -> Objects.equals(pair.getValue(), rowMap.get(pair.getKey())));
    }

    private void deleteContentFromDataFile(Path filePath, Path tempFilePath,
                                           JsonObjectParser parser,
                                           Predicate<Map<String, Object>> filter) {
        try {
            Files.createFile(tempFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not create temp data file", e);
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath);
             BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
            String line = reader.readLine();
            while (line != null) {
                Map<String, Object> obj = parser.fromJson(gson, line);
                if (!filter.test(obj)) {
                    writer.write(line);
                    writer.newLine();
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("can not update data file", e);
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not delete old data file");
        }
        File tempFile = tempFilePath.toFile();
        if (!tempFile.renameTo(filePath.toFile())) {
            throw new IllegalStateException("can not rename temp data file");
        }

    }

    private void updateContentOfDataFile(Path filePath, Path tempFilePath,
                                         JsonObjectParser parser,
                                         JsonObjectMapper mapper,
                                         Predicate<Map<String, Object>> filter,
                                         List<Pair<String, Object>> values) {
        try {
            Files.createFile(tempFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not create temp data file", e);
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath);
             BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
            String line = reader.readLine();
            while (line != null) {
                Map<String, Object> obj = parser.fromJson(gson, line);
                if (filter.test(obj)) {
                    updateRow(obj, values);
                    String newJson = mapper.toJson(gson, obj);
                    writer.write(newJson);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("can not update data file", e);
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not delete old data file");
        }
        File tempFile = tempFilePath.toFile();
        if (!tempFile.renameTo(filePath.toFile())) {
            throw new IllegalStateException("can not rename temp data file");
        }
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
                table = JsonMetadataParser.getParser().fromJson(gson, propertiesJson);
            } catch (IOException e) {
                throw new IllegalStateException("Metadata is corrupted", e);
            }
            metadata.put(table.getKey(), table.getValue());
        }
    }

    private void createDatabaseStructure() {
        try {
            Files.createDirectories(root);
            Files.createDirectories(tablesPath);
            Files.createFile(metadataFilePath);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "can not create database directory-file set", e);
        }
    }

    private void cleanDatabase() {
        try {
            if (Files.exists(root)) {
                FileUtils.deleteDirRecursively(root);
            }
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

    private void createTableFiles(String tableName) {
        try {
            Path tablePath = Paths.get(tablesPath.toString(), tableName);
            Files.createDirectories(tablePath);
            Files.write(Paths.get(tablePath.toString(), SEQUENCE_FILE_NAME), Collections.singletonList("1"));
            for (String partName: getTablePartsNames(tableName)) {
                Files.createFile(Paths.get(tablePath.toString(), partName));
            }
        } catch (IOException e) {
            throw new IllegalStateException("can not create table files", e);
        }
    }

    private void deleteTableFiles(String tableName) {
        try {
            FileUtils.deleteDirRecursively(Paths.get(tablesPath.toString(), tableName));
        } catch (IOException e) {
            throw new IllegalArgumentException("can not delete table files");
        }
    }

    private List<String> getTablePartsNames(String tableName) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < tableFileCount; i++) {
            result.add("_" + tableName + "_part_" + i + ".jsondb");
        }
        return result;
    }

    private String getTablePartName(String tableName, Long id) {
        return "_" + tableName + "_part_" + (id % tableFileCount) + ".jsondb";
    }

    private List<Map<String, Object>> readAllObjects(String tableName) {
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        List<Map<String, Object>> result = new ArrayList<>();
        for (String fileName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, fileName);
            List<String> lines;
            try {
                lines = Files.readAllLines(filePath);
            } catch (IOException e) {
                throw new IllegalStateException("can not read data file", e);
            }
            for (String line: lines) {
                try {
                    result.add(parser.fromJson(gson, line));
                } catch (IOException e) {
                    throw new IllegalStateException("data file is corrupted", e);
                }
            }
        }
        return result;
    }

    private Map<String, Object> readById(String tableName, Long id) {
        JsonObjectParser parser = JsonObjectParser.getParser(metadata.get(tableName));
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line = reader.readLine();
            while (line != null) {
                Map<String, Object> obj = parser.fromJson(gson, line);
                if (obj.get("id").equals(id)) {
                    return obj;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("can not read data file", e);
        }
        return null;
    }

    private void updateRow(Map<String, Object> row, List<Pair<String, Object>> values) {
        for (Pair<String, Object> insertPair: values) {
            String propertyName = insertPair.getKey();
            Object value = insertPair.getValue();
            row.put(propertyName, value);
        }
    }

    private long generateNextId(String tableName) {
        long id = readId(tableName);
        writeId(tableName, id + 1);
        return id;
    }

    private long readId(String tableName) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(tablesPath.toString(), tableName, SEQUENCE_FILE_NAME));
        } catch (IOException e) {
            throw new IllegalStateException("can not read sequence file");
        }
        return Long.valueOf(lines.get(0));
    }

    private void writeId(String tableName, long id) {
        try {
            Files.write(
                    Paths.get(tablesPath.toString(), tableName, SEQUENCE_FILE_NAME),
                    Collections.singletonList(String.valueOf(id))
            );
        } catch (IOException e) {
            throw new IllegalStateException("can change sequence file");
        }
    }

}
