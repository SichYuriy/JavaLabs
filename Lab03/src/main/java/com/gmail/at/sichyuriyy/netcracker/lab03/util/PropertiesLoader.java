package com.gmail.at.sichyuriyy.netcracker.lab03.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Yuriy on 3/19/2017.
 */
public class PropertiesLoader {

    private static final String DB_PROPERTIES = "database.properties";
    private static final String METAMODEL_PROPERTIES = "metamodel.properties";

    private static final PropertiesLoader INSTANCE = new PropertiesLoader();

    private Properties dbProperties;
    private Properties metamodelProperties;

    private PropertiesLoader() {
        dbProperties = loadProperties(DB_PROPERTIES);
        metamodelProperties = loadProperties(METAMODEL_PROPERTIES);
    }

    public static PropertiesLoader getInstance() {
        return INSTANCE;
    }

    public Properties getDbProperties() {
        return dbProperties;
    }

    public Properties getMetamodelProperties() {
        return metamodelProperties;
    }

    public Properties loadProperties(String fileName) {
        Properties properties;
        try (InputStream stream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            properties = loadProperties(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    private Properties loadProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        return properties;
    }
}
