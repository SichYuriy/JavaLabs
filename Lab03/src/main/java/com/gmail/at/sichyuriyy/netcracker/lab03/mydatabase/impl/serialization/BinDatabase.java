package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.serialization;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RecordImpl;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RequestValidator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class BinDatabase implements Database {

    private static final String DB_ROOT = "_dbRoot";
    private static final String METADATA_FILE_NAME = "_metadata.bindb";
    private static final String TABLES_DIRECTORY = "_tables";
    private static final String SEQUENCE_FILE_NAME = "_sequence.bindb";

    private static final int tableFileCount = 5;

    private final Path root;
    private final Path metadataFilePath;
    private final Path tablesPath;

    private boolean storageInitialized;
    private boolean rewriteOldData;

    private Map<String, Map<String, DataType>> metadata;

    private RequestValidator requestValidator;


    public BinDatabase(String rootDirectoryPath, boolean rewriteOldData) {
        this.root = Paths.get(rootDirectoryPath, DB_ROOT);
        this.metadataFilePath = Paths.get(rootDirectoryPath, DB_ROOT, METADATA_FILE_NAME);
        this.tablesPath = Paths.get(rootDirectoryPath, DB_ROOT, TABLES_DIRECTORY);
        this.rewriteOldData = rewriteOldData;
    }

    public BinDatabase(String rootDirectoryPath) {
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
        createTableFiles(name);
        metadata.put(name, propertyMap);
        try (ObjectOutputStream writer =
                     new ObjectOutputStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(metadataFilePath.toFile())))) {
            for (String tableName : metadata.keySet()) {
                writer.writeObject(new Pair<>(tableName, metadata.get(tableName)));
            }
        } catch (IOException e) {
            throw new IllegalStateException("can not change metadata file", e);
        }

    }

    @Override
    public void dropTable(String name) {
        checkInitialization();
        checkTableName(name);
        metadata.remove(name);
        try (ObjectOutputStream writer =
                     new ObjectOutputStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(metadataFilePath.toFile())))) {
            for (String tableName : metadata.keySet()) {
                writer.writeObject(new Pair<>(tableName, metadata.get(tableName)));
            }
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
        Path fileTable = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        List<Map<String, Object>> objects = readAllObjectsFromFile(fileTable);
        objects.add(insertRow);
        try (ObjectOutputStream writer =
                new ObjectOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(fileTable.toFile())))) {
            for (Map<String, Object> row: objects) {
                writer.writeObject(row);
            }
        } catch (IOException e) {
            throw new IllegalStateException("add new data to the file", e);
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
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                "_temp_" + getTablePartName(tableName, id));
        deleteContentFromDataFile(filePath, tempFilePath,
                map -> map.get("id").equals(id));
    }

    @Override
    public void deleteFrom(String tableName, String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertyType(filterName, filterValue,
                metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath,
                    map -> Objects.equals(map.get(filterName), filterValue));
        }

    }

    @Override
    public void deleteFrom(String tableName, List<Pair<String, Object>> filters) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validatePropertiesType(filters, metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath,
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
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath,
                    map -> filter.test(map.get(filterName)));
        }
    }

    @Override
    public void deleteFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.checkProperties(filterNames, metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            deleteContentFromDataFile(filePath, tempFilePath, filter);
        }
    }

    @Override
    public void update(String tableName, Long id, List<Pair<String, Object>> values) {
        checkInitialization();
        checkTableName(tableName);
        requestValidator.validateInsertUpdateRequest(values, metadata.get(tableName));
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                "_temp_" + getTablePartName(tableName, id));
        updateContentOfDataFile(filePath, tempFilePath,
                map -> map.get("id").equals(id),
                values);
    }

    @Override
    public void update(String tableName, List<Pair<String, Object>> values, String filterName, Object filterValue) {
        checkTableName(tableName);
        requestValidator.validateInsertUpdateRequest(values, metadata.get(tableName));
        requestValidator.validatePropertyType(filterName, filterValue,
                metadata.get(tableName));
        for (String filePartName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, filePartName);
            Path tempFilePath = Paths.get(tablesPath.toString(), tableName,
                    "_temp_" + filePartName);
            updateContentOfDataFile(filePath, tempFilePath,
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
                                           Predicate<Map<String, Object>> filter) {
        try {
            Files.createFile(tempFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not create temp data file", e);
        }
        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath.toFile())));
             ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFilePath.toFile())))) {
            while (true) {
                Object readObject = reader.readObject();
                Map<String, Object> obj = (Map<String, Object>)readObject;
                if (!filter.test(obj)) {
                    writer.writeObject(obj);
                }
            }
        } catch (EOFException e) {
            // end of file -> continue work
        } catch (IOException e) {
            throw new IllegalStateException("can not update data file", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("data is corrupted", e);
        }
        try {
            FileUtils.deleteFile(filePath);
//            Files.delete(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not delete old data file", e);
        }
        File tempFile = tempFilePath.toFile();
        if (!tempFile.renameTo(filePath.toFile())) {
            throw new IllegalStateException("can not rename temp data file");
        }

    }

    private void updateContentOfDataFile(Path filePath, Path tempFilePath,
                                         Predicate<Map<String, Object>> filter,
                                         List<Pair<String, Object>> values) {
        try {
            Files.createFile(tempFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not create temp data file", e);
        }
        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath.toFile())));
             ObjectOutputStream writer= new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFilePath.toFile())))) {
            while (true) {
                Object readObject = reader.readObject();
                Map<String, Object> obj = (Map<String, Object>)readObject;
                if (filter.test(obj)) {
                    updateRow(obj, values);
                }
                writer.writeObject(obj);

            }
        } catch (EOFException e) {
            // end of file -> continueWork
        } catch (IOException e) {
            throw new IllegalStateException("can not update data file", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("data is corrupted", e);
        }
        try {
            FileUtils.deleteFile(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("can not delete old data file", e);
        }
        File tempFile = tempFilePath.toFile();
        if (!tempFile.renameTo(filePath.toFile())) {
            throw new IllegalStateException("can not rename temp data file");
        }
    }

    private void readMetadata() {
        metadata = new HashMap<>();
        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(metadataFilePath.toFile())))) {
            while (true) {
                Object readObject = reader.readObject();
                Pair<String, Map<String, DataType>> table = (Pair<String, Map<String, DataType>>)readObject;
                metadata.put(table.getKey(), table.getValue());
            }
        } catch (EOFException e) {
            // end of file -> continue work
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("can not read metadata", e);
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
                FileUtils.deleteDirHard(root);
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
            FileUtils.deleteDirHard(Paths.get(tablesPath.toString(), tableName));
        } catch (IOException e) {
            throw new IllegalArgumentException("can not delete table files", e);
        }
    }

    private List<String> getTablePartsNames(String tableName) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < tableFileCount; i++) {
            result.add("_" + tableName + "_part_" + i + ".bindb");
        }
        return result;
    }

    private String getTablePartName(String tableName, Long id) {
        return "_" + tableName + "_part_" + (id % tableFileCount) + ".bindb";
    }

    private List<Map<String, Object>> readAllObjects(String tableName) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (String fileName: getTablePartsNames(tableName)) {
            Path filePath = Paths.get(tablesPath.toString(), tableName, fileName);
            result.addAll(readAllObjectsFromFile(filePath));
        }
        return result;
    }

    private List<Map<String, Object>> readAllObjectsFromFile(Path filePath) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath.toFile())))) {
            while (true) {
                Object obj = reader.readObject();
                result.add((Map<String, Object>) obj);
            }
        } catch (EOFException e) {
            // end of file continue work
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("can not read data file", e);
        }
        return result;
    }


    private Map<String, Object> readById(String tableName, Long id) {
        Path filePath = Paths.get(tablesPath.toString(), tableName, getTablePartName(tableName, id));
        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath.toFile())))) {
            while (true) {
                Object readObject = reader.readObject();
                Map<String, Object> obj = (Map<String, Object>)readObject;
                if (obj.get("id").equals(id)) {
                    return obj;
                }
            }
        } catch (EOFException e) {
            // end of file continue work
        } catch (IOException e) {
            throw new IllegalStateException("can not read data file", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("data is corrupted", e);
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
