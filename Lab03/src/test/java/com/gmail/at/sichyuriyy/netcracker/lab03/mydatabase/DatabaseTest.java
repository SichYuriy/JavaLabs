package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.RecordImpl;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yuriy on 17.02.2017.
 */
public abstract class DatabaseTest {

    private Database database;

    protected abstract Database getTestedDatabase();
    protected abstract void releaseResources();

    @Before
    public void setUp() throws Exception {
        database = getTestedDatabase();
        database.initStorage();
    }

    @After
    public void tearDown() throws Exception {
        database = null;
        releaseResources();
    }

    @Test
    public void createTable() throws Exception {
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("name", DataType.STRING));
        properties.add(new Pair<>("age", DataType.INTEGER));
        database.createTable("table1", properties);
        List<Pair<String, DataType>> expectedProperties =
                new ArrayList<>();
        expectedProperties.addAll(properties);
        expectedProperties.add(new Pair<>("id", DataType.LONG));
        List<Pair<String, DataType>> actualProperties
                = database.describeTable("table1");
        boolean equals = checkEqualsCollections(expectedProperties, actualProperties);
        assertTrue(equals);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTableDuplication() throws Exception {
        insertTestData(database);
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("name", DataType.STRING));
        database.createTable("table1", properties);
    }

    @Test
    public void dropTable() throws Exception {
        insertTestData(database);
        database.dropTable("table1");
        List<Pair<String, DataType>> properties = database.describeTable("table1");
        int expectedSize = 0;
        int actualSize = properties.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test(expected = NoSuchElementException.class)
    public void dropTableNoExist() throws Exception {
        insertTestData(database);
        database.dropTable("table777");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values = new ArrayList<>();
    }

    @Test
    public void selectFrom() throws Exception {
        insertTestData(database);
        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 1L));
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 23));
        }};
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values1));
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void selectFromById() throws Exception {
        insertTestData(database);
        Long id = 1L;
        Record actualRecord = database.selectFrom("table1", id);
        List<Pair<String, Object>> values = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 1L));
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 23));
        }};
        Record expectedRecord = new RecordImpl(values);
        boolean equals = checkEqualsRecord(expectedRecord, actualRecord);
        assertTrue(equals);
    }

    @Test
    public void selectFromWithFilter() throws Exception {
        insertTestData(database);
        String filterName = "name";
        String filterValue = "name2";
        List<Record> actualResultSet = database.selectFrom("table1",
                filterName, filterValue);
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        expectedResultSet.add(new RecordImpl(values));
        boolean equals = checkEqualsRecordList(expectedResultSet, actualResultSet);
        assertTrue(equals);

    }

    @Test
    public void selectFromWithFilterEmptyResult() throws Exception {
        insertTestData(database);
        String filterName = "name";
        String filterValue = "name42";
        List<Record> actualResultSet = database.selectFrom("table1",
                filterName, filterValue);
        int actualSize = actualResultSet.size();
        int expectedSize = 0;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void selectFromWithFilters() throws Exception {
        insertTestData(database);
        List<Pair<String, Object>> filters  = new ArrayList<>();
        filters.add(new Pair<>("name", "name2"));
        filters.add(new Pair<>("age", 24));
        filters.add(new Pair<>("id", 2L));

        List<Record> actualResultSet = database.selectFrom("table1", filters);
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        expectedResultSet.add(new RecordImpl(values));
        boolean equals = checkEqualsRecordList(expectedResultSet, actualResultSet);
        assertTrue(equals);
    }

    @Test
    public void selectFromWithPredicateFilter() throws Exception {
        insertTestData(database);
        String filterName = "age";
        List<Record> actualResultSet = database.selectFrom("table1",
                filterName, age -> (Integer)age <= 23 && (Integer)age >= 20);
        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 23));
            add(new Pair<>("id", 1L));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
            add(new Pair<>("id", 3L));
        }};
        List<Record> expectedResultSet = new ArrayList<>();
        expectedResultSet.add(new RecordImpl(values1));
        expectedResultSet.add(new RecordImpl(values3));
        boolean equals = checkEqualsRecordList(expectedResultSet, actualResultSet);
        assertTrue(equals);
    }

    @Test
    public void selectFromMapPredicate() throws Exception {
        insertTestData(database);
        List<String> filterNames = new ArrayList<>();
        filterNames.add("name");
        filterNames.add("age");
        List<Record> actualResultSet = database.selectFrom("table1",
                filterNames,
                map -> map.get("name").equals("name1") || map.get("age").equals(21));
        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 23));
            add(new Pair<>("id", 1L));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
            add(new Pair<>("id", 3L));
        }};
        List<Record> expectedResultSet = new ArrayList<>();
        expectedResultSet.add(new RecordImpl(values1));
        expectedResultSet.add(new RecordImpl(values3));
        boolean equals = checkEqualsRecordList(expectedResultSet, actualResultSet);
        assertTrue(equals);
    }

    @Test
    public void deleteFrom() throws Exception {
        insertTestData(database);
        database.deleteFrom("table1");
        List<Record> actualResultSet = database.selectFrom("table1");
        int actualSize = actualResultSet.size();
        int expectedSize = 0;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void deleteFromById() throws Exception {
        insertTestData(database);
        database.deleteFrom("table1", 1L);

        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void deleteFromByFilter() throws Exception {
        insertTestData(database);
        database.deleteFrom("table1", "name", "name1");

        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void deleteFromByFilters() throws Exception {
        insertTestData(database);
        List<Pair<String, Object>> filters = new ArrayList<>();
        filters.add(new Pair<>("name", "name1"));
        filters.add(new Pair<>("age", 23));
        database.deleteFrom("table1", filters);

        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void deleteFromByPredicate() throws Exception {
        insertTestData(database);
        database.deleteFrom("table1", "name",
                name -> ((String)name).contains("1"));

        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void deleteFromByMapPredicate() throws Exception {
        insertTestData(database);
        List<String> filterNames = new ArrayList<>();
        filterNames.add("name");
        filterNames.add("age");
        database.deleteFrom("table1", filterNames,
                map -> map.get("name").equals("name1") || map.get("age").equals(21));

        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void update() throws Exception {
        insertTestData(database);
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("age", 50));
        long id = 1L;
        database.update("table1", id, values);
        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 1L));
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 50));
        }};
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values1));
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }

    @Test
    public void updateByFilter() throws Exception {
        insertTestData(database);
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("age", 50));
        String filterName = "name";
        String filtersValue = "name1";
        database.update("table1", values, filterName, filtersValue);
        List<Record> resultSet = database.selectFrom("table1");
        List<Record> expectedResultSet = new ArrayList<>();
        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 1L));
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 50));
        }};
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 2L));
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 3L));
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("id", 4L));
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        expectedResultSet.add(new RecordImpl(values1));
        expectedResultSet.add(new RecordImpl(values2));
        expectedResultSet.add(new RecordImpl(values3));
        expectedResultSet.add(new RecordImpl(values4));

        boolean equals = checkEqualsRecordList(expectedResultSet, resultSet);
        assertTrue(equals);

    }


    private void insertTestData(Database database) {
        List<Pair<String, DataType>> properties = new ArrayList<>();
        properties.add(new Pair<>("name", DataType.STRING));
        properties.add(new Pair<>("age", DataType.INTEGER));

        database.createTable("table1", properties);

        List<Pair<String, Object>> values1 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name1"));
            add(new Pair<>("age", 23));
        }};
        List<Pair<String, Object>> values2 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name2"));
            add(new Pair<>("age", 24));
        }};
        List<Pair<String, Object>> values3 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name3"));
            add(new Pair<>("age", 21));
        }};
        List<Pair<String, Object>> values4 = new ArrayList<Pair<String, Object>>(){{
            add(new Pair<>("name", "name4"));
            add(new Pair<>("age", 27));
        }};
        database.insertInto("table1", values1);
        database.insertInto("table1", values2);
        database.insertInto("table1", values3);
        database.insertInto("table1", values4);


    }

    private boolean checkEqualsCollections(Collection<?> expected, Collection<?> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        boolean equals;

        equals = actual.stream()
                .allMatch(expected::contains);
        if (equals) {
            equals = expected.stream()
                    .allMatch(actual::contains);
        }
        return equals;
    }

    private boolean checkEqualsRecord(Record expected, Record actual) {
        return checkEqualsCollections(expected.getAll(), actual.getAll());
    }

    private boolean checkEqualsRecordList(List<Record> expected, List<Record> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        boolean equals;
        equals = actual.stream()
                .allMatch(actualRecord -> containsRecord(expected, actualRecord));
        if (equals) {
            equals = expected.stream()
                    .allMatch(expectedRecord -> containsRecord(actual, expectedRecord));
        }
        return equals;
    }

    private boolean containsRecord(List<Record> list, Record record) {
        return list.stream()
                .anyMatch(temp -> checkEqualsRecord(record, temp));
    }

}