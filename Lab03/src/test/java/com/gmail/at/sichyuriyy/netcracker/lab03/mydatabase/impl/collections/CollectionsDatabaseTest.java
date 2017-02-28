package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DatabaseTest;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 17.02.2017.
 */
public class CollectionsDatabaseTest extends DatabaseTest {

    @Override
    protected Database getTestedDatabase() {
        return CollectionsDatabase.getCollectionsDatabase();
    }

    @Override
    protected void releaseResources() {

    }
}