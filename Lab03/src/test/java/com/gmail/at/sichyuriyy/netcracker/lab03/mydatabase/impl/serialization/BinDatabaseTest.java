package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.serialization;

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
public class BinDatabaseTest extends DatabaseTest {
    private static final String ROOT = "BinaryDatabaseRoot";

    @Override
    protected Database getTestedDatabase() {
        return new BinDatabase(ROOT, true);
    }

    @Override
    protected void releaseResources() {
        try {
            FileUtils.deleteDirRecursively(Paths.get(ROOT));
        } catch (IOException e) {
            releaseResources();
        }
    }
}