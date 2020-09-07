package com.springApp.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static Properties readPropertiesFile() throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream("src/main/resources/application.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }
        return prop;
    }

    public static String getProperties(String propName) {
        Properties prop = null;
        try {
            prop = readPropertiesFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert prop != null;
        return prop.getProperty(propName);
    }
}
