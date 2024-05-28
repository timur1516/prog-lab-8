package common.Controllers;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Class to work with properties files
 */
public class PropertiesFilesController {
    /**
     * Method to read data from properties fie
     * @param fileName Name of file to read
     * @return Properties object which was read
     * @throws IOException If any I\O error occurred
     */
    public Properties readProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(fileName);
        properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return properties;
    }

    public Properties readResource(String fileName) throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if(inputStream == null){
            throw new IOException();
        }
        properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return properties;
    }

    /**
     * Method to white data to properties file
     * @param properties Data to write
     * @param filename Name of file
     * @param comments Comments to mention in file
     * @throws IOException If any I\O error occurred while writing to file
     */
    public void writeProperties(Properties properties, String filename, String comments) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        properties.store(outputStream, comments);
    }
}
