package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import client.Commands.*;
import client.GUI.GUIController;
import client.net.UDPClient;
import common.Controllers.CommandsController;
import client.Readers.WorkerReader;
import common.Controllers.PropertiesFilesController;
import common.UI.Console;

import javax.swing.*;

/**
 * Main app class
 * <p>Completes initialization of all controllers, sets default input stream for Console
 * <p>In the beginning starts udp client, then calls interactiveMode method
 */
public class Main {
    /**
     * Main method of program
     * <p>Init all controllers, starts udp client, and start handling user commands
     * @param args (not used)
     */
    public static void main(String[] args) {
        Console.getInstance().setScanner(new Scanner(System.in));

        GUIController.getInstance();

        new CollectionUpdaterTask().start();

        int serverPort = Constants.DEFAULT_PORT_NUMBER;
        try {
            serverPort = readServerPort();
            GUIController.getInstance().showInfoMessage(String.format("Server port set to: %d", serverPort));
        } catch (IOException | NumberFormatException e) {
            GUIController.getInstance().showWarningMessage("Error while reading config file!\n" +
                    "Server port set to default value: 8081\n" +
                    "If you want to change it, create config.properties file and restart program");
        }

        try {
            UDPClient.getInstance().init(InetAddress.getLocalHost(), serverPort, Constants.CLIENT_TIMEOUT);
            UDPClient.getInstance().open();
        } catch (UnknownHostException e) {
            GUIController.getInstance().showErrorMessage("Server host was not found!");
            System.exit(0);
        } catch (SocketException e) {
            GUIController.getInstance().showErrorMessage("Error while starting client!");
            System.exit(0);
        }
    }

    /**
     * Method to read server port from properties config file
     * @return Integer server port
     * @throws IOException If any I\O error occurred
     * @throws NumberFormatException If port number unparsable for Integer
     */
    private static int readServerPort() throws IOException, NumberFormatException {
        Properties configProperties = new PropertiesFilesController().readProperties("config.properties");
        return Integer.parseInt(configProperties.getProperty("port"));
    }
}