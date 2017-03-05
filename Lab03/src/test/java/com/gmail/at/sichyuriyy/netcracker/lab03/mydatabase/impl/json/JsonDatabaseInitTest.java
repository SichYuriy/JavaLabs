package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json;

import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class JsonDatabaseInitTest {

    private static final String TABLE1_JSON = "{" +
                "\"tableName\":\"table1\"," +
                "\"properties\":{" +
                    "\"id\":\"LONG\"," +
                    "\"name\":\"STRING\"," +
                    "\"age\":\"INTEGER\"" +
                "}" +
            "}";
    private static final String TABLE2_JSON = "{" +
                "\"tableName\":\"table2\"," +
                "\"properties\":{" +
                    "\"id\":\"LONG\"," +
                    "\"name\":\"STRING\"" +
                "}" +
            "}";

    private List<Pair<String, DataType>> table1Props = new ArrayList<Pair<String, DataType>>(){{
        add(new Pair<>("id", DataType.LONG));
        add(new Pair<>("name", DataType.STRING));
        add(new Pair<>("age", DataType.INTEGER));
    }};

    private List<Pair<String, DataType>> table2Props = new ArrayList<Pair<String, DataType>>(){{
        add(new Pair<>("id", DataType.LONG));
        add(new Pair<>("name", DataType.STRING));
    }};



    @Before
    public void setUp() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(TABLE1_JSON);
        lines.add(TABLE2_JSON);
        Files.createDirectories(Paths.get("testDbRoot\\_dbRoot"));
        Files.write(Paths.get("testDbRoot\\_dbRoot\\_metadata.jsondb"), lines);

    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirRecursively(Paths.get("testDbRoot"));
    }

    @Test
    public void initFromOldDatabase() {

        Database database = new JsonDatabase("testDbRoot", false);
        database.initStorage();

        List<Pair<String, DataType>> expectedProperties = table1Props;
        List<Pair<String, DataType>> actualProperties = database.describeTable("table1");

        assertTrue(TestUtils.equalContentCollections(expectedProperties, actualProperties));
    }

    @Test
    public void initFromOldDatabase2() {

        Database database = new JsonDatabase("testDbRoot", false);
        database.initStorage();

        List<Pair<String, DataType>> expectedProperties = table2Props;
        List<Pair<String, DataType>> actualProperties = database.describeTable("table2");
        assertTrue(TestUtils.equalContentCollections(expectedProperties, actualProperties));
    }

    @Test
    public void initFromOldDatabaseNonExistTable() {

        Database database = new JsonDatabase("testDbRoot", false);
        database.initStorage();

        List<Pair<String, DataType>> actualProperties = database.describeTable("table3");

        assertTrue(actualProperties.isEmpty());
    }
}