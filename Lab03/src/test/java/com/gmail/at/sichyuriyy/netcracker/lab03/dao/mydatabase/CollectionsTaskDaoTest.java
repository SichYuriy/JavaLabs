package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.TaskDaoTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl.MyDatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.impl.MyDatabaseStructureCreator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections.CollectionsDatabase;

/**
 * Created by Yuriy on 27.02.2017.
 */
public class CollectionsTaskDaoTest extends TaskDaoTest {

    private MyDatabaseStructureCreator structureCreator = new MyDatabaseStructureCreator();

    @Override
    protected DatabaseConnector getTestedDatabaseConnector() {
        Database database = CollectionsDatabase.getCollectionsDatabase();
        structureCreator.createTaskManagerStructure(database);
        MyDatabaseDaoFactory daoFactory = new MyDatabaseDaoFactory(database);
        DatabaseConnector databaseConnector = new MyDatabaseConnector(daoFactory);
        return databaseConnector;
    }

    @Override
    protected void cleanResources() {

    }
}