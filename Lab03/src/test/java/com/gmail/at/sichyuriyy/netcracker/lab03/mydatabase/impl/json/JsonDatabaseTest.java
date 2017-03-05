package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DatabaseTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Yuriy on 03.03.2017.
 */
public class JsonDatabaseTest extends DatabaseTest {

    private static final String ROOT = "JsonDatabaseRoot";

    @Override
    protected Database getTestedDatabase() {
        return new JsonDatabase(ROOT, true);
    }

    @Override
    protected void releaseResources() throws IOException {
        FileUtils.deleteDirRecursively(Paths.get(ROOT));
    }
}