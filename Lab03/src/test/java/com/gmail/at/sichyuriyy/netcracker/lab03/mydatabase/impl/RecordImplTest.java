package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Record;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 15.02.2017.
 */
public class RecordImplTest {

    private static final double DELTA = 10e-7;

    private RecordImpl record;
    private List<Pair<String, Object>> values;

    @Before
    public void setUp() throws Exception {
        values = new ArrayList<>();
        values.add(new Pair<>("long", 1L));
        values.add(new Pair<>("string", "str"));
        values.add(new Pair<>("int", 2));
        values.add(new Pair<>("double", 3.1d));
        values.add(new Pair<>("bool", Boolean.TRUE));
        values.add(new Pair<>("date", new Date(0)));
        record = new RecordImpl(values);
    }

    @After
    public void tearDown() throws Exception {
        record = null;
        values = null;

    }

    @Test
    public void getString() throws Exception {
        String expected = "str";
        String actual = record.getString("string");
        assertEquals(expected, actual);
    }

    @Test
    public void getLong() throws Exception {
        Long expected = 1L;
        Long actual = record.getLong("long");
        assertEquals(expected, actual);
    }

    @Test
    public void getInteger() throws Exception {
        Integer expected = 2;
        Integer actual = record.getInteger("int");
        assertEquals(expected, actual);
    }

    @Test
    public void getDouble() throws Exception {
        Double expected = 3.1d;
        Double actual = record.getDouble("double");
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void getDate() throws Exception {
        Date expected = new Date(0);
        Date actual = record.getDate("date");
        assertEquals(expected, actual);
    }

    @Test
    public void getBoolean() throws Exception {
        Boolean expected = true;
        Boolean actual = record.getBoolean("bool");
        assertEquals(expected, actual);
    }

    @Test
    public void join() throws Exception {
        List<Pair<String, Object>> joinValues = generateJoinValues();
        RecordImpl joinRecord = new RecordImpl(joinValues);
        RecordImpl joinResult = (RecordImpl) record.join(joinRecord,
                "Left", "Right");
        List<Pair<String, Object>> expectedResultValues = new ArrayList<>();

        List<Pair<String, Object>> leftValues = values.stream()
                .map(pair -> new Pair<>("Left." + pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        List<Pair<String, Object>> rightValues = joinValues.stream()
                .map(pair -> new Pair<>("Right." + pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        expectedResultValues.addAll(leftValues);
        expectedResultValues.addAll(rightValues);

        List<Pair<String, Object>> actualResultValues = joinResult.getAll();
        boolean equals;
        equals = actualResultValues.stream()
                .allMatch(expectedResultValues::contains);
        if (equals) {
            equals = expectedResultValues.stream()
                    .allMatch(actualResultValues::contains);
        }
        assertTrue(equals);


    }

    @Test
    public void getAll() throws Exception {
        List<Pair<String, Object>> expectedList = values;
        List<Pair<String, Object>> actualList = record.getAll();
        boolean equals;
        equals = actualList.stream()
                .allMatch(expectedList::contains);
        if (equals) {
            equals = expectedList.stream()
                    .allMatch(actualList::contains);
        }
        assertTrue(equals);
    }

    @Test
    public void getLeftAfterJoin() throws Exception {
        RecordImpl right = new RecordImpl(generateJoinValues());
        Record joinResult = record.join(right,
                "Left", "Right");
        String expected = "str";
        String actual = joinResult.getString("Left.string");
        assertEquals(expected, actual);
    }

    @Test
    public void getRightAfterJoin() throws Exception {
        RecordImpl right = new RecordImpl(generateJoinValues());
        Record joinResult = record.join(right,
                "Left", "Right");
        String expected = "joinStrVal";
        String actual = joinResult.getString("Right.joinStr");
        assertEquals(expected, actual);
    }

    @Test
    public void getAfterJoin() throws Exception {
        RecordImpl right = new RecordImpl(generateJoinValues());
        Record joinResult = record.join(right,
                "Left", "Right");
        String expected = "joinStrVal";
        String actual = joinResult.getString("joinStr");
        assertEquals(expected, actual);
    }

    private List<Pair<String, Object>> generateJoinValues() {
        List<Pair<String, Object>> values = new ArrayList<>();
        values.add(new Pair<>("joinStr", "joinStrVal"));
        values.add(new Pair<>("joinLong", 22L));
        return values;
    }

}