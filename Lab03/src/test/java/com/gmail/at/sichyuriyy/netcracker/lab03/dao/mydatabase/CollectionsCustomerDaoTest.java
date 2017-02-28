package com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase;

import com.gmail.at.sichyuriyy.netcracker.lab03.dao.CustomerDaoTest;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactory;
import com.gmail.at.sichyuriyy.netcracker.lab03.dao.mydatabase.factory.MyDatabaseDaoFactoryImpl;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.impl.CollectionsDatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseinit.impl.MyDatabaseStructureCreator;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.Database;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.collections.CollectionsDatabase;

/**
 * Created by Yuriy on 25.02.2017.
 */
public class CollectionsCustomerDaoTest extends CustomerDaoTest {

    private MyDatabaseStructureCreator structureCreator = new MyDatabaseStructureCreator();


    @Override
    protected DatabaseConnector getTestedDatabaseConnector() {
        Database database = CollectionsDatabase.getCollectionsDatabase();
        structureCreator.createTaskManagerStructure(database);
        MyDatabaseDaoFactory daoFactory = new MyDatabaseDaoFactoryImpl(database);
        DatabaseConnector databaseConnector = new CollectionsDatabaseConnector(daoFactory);
        return databaseConnector;
    }

    @Override
    protected void cleanResources() {

    }
}