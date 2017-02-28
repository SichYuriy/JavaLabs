package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RecordImpl;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RequestValidator;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Yuriy on 10.02.2017.
 */
public class CollectionsDatabase implements Database {

    private boolean storageInitialized;

    private Map<String, Table> tables;
    private RequestValidator requestValidator;

    private CollectionsDatabase() {
    }

    public static CollectionsDatabase getCollectionsDatabase() {
        CollectionsDatabase collectionsDatabase = new CollectionsDatabase();
        collectionsDatabase.initStorage();
        return collectionsDatabase;
    }

    @Override
    public void initStorage() {
        storageInitialized = true;
        tables = new HashMap<>();
        requestValidator = new RequestValidator();
    }

    @Override
    public void createTable(String name, List<Pair<String, DataType>> properties) {
        checkInitialization();
        checkTableNameAbsent(name);
        requestValidator.validateCreateTableRequest(properties);
        List<Pair<String, DataType>> propertiesCopy = new ArrayList<>(properties);
        propertiesCopy.add(new Pair<>("id", DataType.LONG));
        tables.put(name, new Table(propertiesCopy));
    }

    @Override
    public void dropTable(String name) {
        checkInitialization();
        checkTableName(name);
        tables.remove(name);
    }

    @Override
    public List<Pair<String, DataType>> describeTable(String tableName) {
        checkInitialization();
        List<Pair<String, DataType>> result = new ArrayList<>();
        Table table = tables.get(tableName);
        if (table == null) {
            return result;
        }
        Map<String, DataType> properties = table.getProperties();
        for (String propertyName: properties.keySet()) {
            result.add(new Pair<>(propertyName, properties.get(propertyName)));
        }
        return result;
    }

    @Override
    public List<Record> selectFrom(String tableName) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        return table.getRows().values().stream()
                .map(Map::entrySet)
                .map(this::mapEntrySet)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    private List<Pair<String, Object>> mapEntrySet(Collection<Map.Entry<String, Object>> entries) {
        return entries.stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Record selectFrom(String tableName, Long id) {
        checkInitialization();
        checkTableName(tableName);
        Map<String, Object> row = tables.get(tableName).getRows().get(id);
        if (row == null) {
            return null;
        }
        return new RecordImpl(mapEntrySet(row.entrySet()));
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validatePropertyType(filterName, filterValue,
                table.getProperties());
        return table.getRows().values().stream()
                .filter(rowMap -> Objects.equals(rowMap.get(filterName), filterValue))
                .map(Map::entrySet)
                .map(this::mapEntrySet)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> selectFrom(String tableName, List<Pair<String, Object>> filters) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validatePropertiesType(filters, table.getProperties());

        return table.getRows().values().stream()
                .filter(rowMap -> checkRow(rowMap, filters))
                .map(Map::entrySet)
                .map(this::mapEntrySet)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    private boolean checkRow(Map<String, Object> rowMap,
                             List<Pair<String, Object>> filters) {
        return filters.stream()
                .allMatch(pair -> Objects.equals(pair.getValue(), rowMap.get(pair.getKey())));
    }

    @Override
    public List<Record> selectFrom(String tableName, String filterName, Predicate filter) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.checkProperty(filterName, table.getProperties());
        return table.getRows().values().stream()
                .filter((rowMap) -> filter.test(rowMap.get(filterName)))
                .map(Map::entrySet)
                .map(this::mapEntrySet)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Record> selectFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.checkProperties(filterNames, table.getProperties());
        return table.getRows().values().stream()
                .filter(filter)
                .map(Map::entrySet)
                .map(this::mapEntrySet)
                .map(RecordImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public Long insertInto(String tableName, List<Pair<String, Object>> values) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        Map<String, DataType> properties = table.getProperties();
        requestValidator.validateInsertUpdateRequest(values, properties);
        Map<String, Object> insertRow = new HashMap<>();
        properties.keySet().forEach(propertyName -> insertRow.put(propertyName, null));
        updateRow(insertRow, values);
        Long id = table.getNextId();
        table.generateNextId();
        insertRow.put("id",id);
        table.getRows().put(id, insertRow);
        return id;
    }

    @Override
    public void deleteFrom(String tableName) {
        checkInitialization();
        checkTableName(tableName);
        tables.get(tableName).getRows().clear();
    }

    @Override
    public void deleteFrom(String tableName, Long id) {
        checkInitialization();
        checkTableName(tableName);
        tables.get(tableName).getRows().remove(id);
    }

    @Override
    public void deleteFrom(String tableName, String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validatePropertyType(filterName, filterValue,
                table.getProperties());
        Map<Long, Map<String, Object>> rows = table.getRows();
        Set<Long> idSet = new HashSet<>(rows.keySet());
        idSet.stream()
                .filter((id) -> Objects.equals(rows.get(id).get(filterName), filterValue))
                .forEach(rows::remove);
    }

    @Override
    public void deleteFrom(String tableName, List<Pair<String, Object>> filters) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validatePropertiesType(filters, table.getProperties());
        Map<Long, Map<String, Object>> rows = table.getRows();
        Set<Long> idSet = new HashSet<>(rows.keySet());
        idSet.stream()
                .filter((id) -> checkRow(rows.get(id), filters))
                .forEach(rows::remove);
    }

    @Override
    public void deleteFrom(String tableName, String filterName, Predicate filter) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.checkProperty(filterName, table.getProperties());
        Map<Long, Map<String, Object>> rows = table.getRows();
        Set<Long> idSet = new HashSet<>(rows.keySet());
        idSet.stream()
                .filter((id) -> filter.test(rows.get(id).get(filterName)))
                .forEach(rows::remove);
    }

    @Override
    public void deleteFrom(String tableName, List<String> filterNames, Predicate<Map<String, Object>> filter) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.checkProperties(filterNames, table.getProperties());
        Map<Long, Map<String, Object>> rows = table.getRows();
        Set<Long> idSet = new HashSet<>(rows.keySet());
        idSet.stream()
                .filter((id) -> filter.test(rows.get(id)))
                .forEach(rows::remove);
    }

    @Override
    public void update(String tableName, Long id, List<Pair<String, Object>> values) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validateInsertUpdateRequest(values, table.getProperties());
        Map<String, Object> row = table.getRows().get(id);
        if (row == null) {
            return;
        }
        updateRow(row, values);
    }

    @Override
    public void update(String tableName, List<Pair<String, Object>> values,
                       String filterName, Object filterValue) {
        checkInitialization();
        checkTableName(tableName);
        Table table = tables.get(tableName);
        requestValidator.validateInsertUpdateRequest(values, table.getProperties());
        requestValidator.validatePropertyType(filterName, filterValue,
                table.getProperties());
        table.getRows().values().stream()
                .filter(rowMap -> rowMap.get(filterName).equals(filterValue))
                .forEach(row -> updateRow(row, values));

    }

    @Override
    public boolean tableExists(String tableName) {
        return tables.get(tableName) != null;
    }

    private void updateRow(Map<String, Object> row, List<Pair<String, Object>> values) {
        for (Pair<String, Object> insertPair: values) {
            String propertyName = insertPair.getKey();
            Object value = insertPair.getValue();
            row.put(propertyName, value);
        }
    }

    private void checkInitialization() {
        if (!storageInitialized) {
            throw new IllegalStateException("storage not initialized(init storage"
                    + "must be called before)");
        }
    }

    private void checkTableNameAbsent(String tableName) {
        if (tables.containsKey(tableName)) {
            throw new IllegalArgumentException("table " + tableName + " is already exist");
        }
    }

    private void checkTableName(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new NoSuchElementException();
        }
    }
}
