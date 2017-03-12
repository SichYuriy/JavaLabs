package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DatabaseTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.json.JsonDatabase;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class XmlDatabaseTest extends DatabaseTest {
    private static final String ROOT = "XmlDatabaseRoot";

    @Override
    protected Database getTestedDatabase() {
        return new XmlDatabase(ROOT, true);
    }

    @Override
    protected void releaseResources() throws IOException {
        FileUtils.deleteDirRecursively(Paths.get(ROOT));
    }
}