package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.EmployeeDaoTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl.MyDatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.impl.MyDatabaseStructureCreator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.serialization.BinDatabase;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Yuriy on 3/13/2017.
 */
public class SerializationEmployeeDaoTest extends EmployeeDaoTest {

    private static final String ROOT = "BinDatabaseRoot_EmployeeDaoTest";
    private MyDatabaseStructureCreator structureCreator = new MyDatabaseStructureCreator();

    @Override
    protected DatabaseConnector getTestedDatabaseConnector() {
        Database database = new BinDatabase(ROOT, true);
        database.initStorage();
        structureCreator.createTaskManagerStructure(database);
        MyDatabaseDaoFactory daoFactory = new MyDatabaseDaoFactory(database);
        return new MyDatabaseConnector(daoFactory);
    }

    @Override
    protected void cleanResources() {
        try {
            FileUtils.deleteDirRecursively(Paths.get(ROOT));
        } catch (IOException e) {
            cleanResources();
        }
    }

}