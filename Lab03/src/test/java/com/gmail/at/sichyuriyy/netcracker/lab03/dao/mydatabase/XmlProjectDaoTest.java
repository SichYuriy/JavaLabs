package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.ProjectDaoTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactoryImpl;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl.CollectionsDatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.impl.MyDatabaseStructureCreator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.XmlDatabase;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 3/12/2017.
 */
public class XmlProjectDaoTest extends ProjectDaoTest {

    private static final String ROOT = "XmlDatabaseRoot_ProjectDaoTest";
    private MyDatabaseStructureCreator structureCreator = new MyDatabaseStructureCreator();

    @Override
    protected DatabaseConnector getTestedDatabaseConnector() {
        Database database = new XmlDatabase(ROOT, true);
        database.initStorage();
        structureCreator.createTaskManagerStructure(database);
        MyDatabaseDaoFactory daoFactory = new MyDatabaseDaoFactoryImpl(database);
        return new CollectionsDatabaseConnector(daoFactory);
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