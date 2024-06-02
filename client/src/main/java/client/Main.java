package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;

import client.GUI.GUIController;
import client.net.UDPClient;
import common.Controllers.PropertiesFilesController;
import common.Exceptions.LocalizedException;
import common.Exceptions.LocalizedMessage;
import common.UI.Console;

import javax.swing.*;

/**
 * Main app class
 * <p>Completes initialization of all controllers, sets default input stream for Console
 * <p>In the beginning starts udp client, then calls interactiveMode method
 */
public class Main {
    private static int serverPort;
    private static InetAddress serverHost;

    /**
     * Main method of program
     * <p>Init all controllers, starts udp client, and start handling user commands
     * @param args (not used)
     */
    public static void main(String[] args) {
        Console.getInstance().setScanner(new Scanner(System.in));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        GUIController.getInstance();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new CollectionUpdaterTask(), 1, 5, TimeUnit.SECONDS);


        try {
            serverPort = Constants.DEFAULT_PORT_NUMBER;
            serverHost = InetAddress.getLocalHost();
            readServerData();
            UDPClient.getInstance().init(serverHost, serverPort, Constants.CLIENT_TIMEOUT);
            UDPClient.getInstance().open();
        } catch (UnknownHostException e) {
            GUIController.getInstance().showErrorMessage(new LocalizedException("serverHostNotFound"));
            System.exit(0);
        } catch (SocketException e) {
            GUIController.getInstance().showErrorMessage(new LocalizedException("clientStartError"));
            System.exit(0);
        }
    }

    private static void readServerData() {
        try {
            Properties configProperties = new PropertiesFilesController().readResource("config.properties");
            serverPort = Integer.parseInt(configProperties.getProperty("port"));
            serverHost = InetAddress.getByName(configProperties.getProperty("host"));
            GUIController.getInstance().showInfoMessage(new LocalizedMessage("serverPortMessage", serverPort));
        } catch (IOException | NumberFormatException e) {
            GUIController.getInstance().showWarningMessage(new LocalizedMessage("portConfigMessage", serverPort));
        }

    }
}