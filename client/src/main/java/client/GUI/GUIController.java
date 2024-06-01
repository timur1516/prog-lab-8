package client.GUI;

import client.Controllers.ResourceBundlesController;
import common.Exceptions.LocalizedException;
import common.Exceptions.LocalizedMessage;
import common.net.requests.ServerResponse;

import javax.swing.*;

/**
 * Singleton class to control screens and show messages
 */
public class GUIController {
    private static GUIController GUI_CONTROLLER = null;
    private final JFrame mainFrame;
    private GUIStates state;

    public static GUIController getInstance(){
        if(GUI_CONTROLLER == null){
           GUI_CONTROLLER = new GUIController();
        }
        return GUI_CONTROLLER;
    }

    /**
     * Constructor of guiController
     * <p>It create the main Frame set state to Log in screen
     */
    private GUIController(){
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);

        state = GUIStates.LOG_IN;

        draw();
    }

    /**
     * Method to draw currently selected screen
     */
    private void draw(){
        JComponent contentPanel = switch (state){
            case MAIN -> MainForm.getInstance().$$$getRootComponent$$$();
            case LOG_IN -> (new LogInForm()).$$$getRootComponent$$$();
            case SIGN_UP -> (new SignUpForm().$$$getRootComponent$$$());
        };
        mainFrame.setContentPane(contentPanel);
        mainFrame.setVisible(true);
    }

    /**
     * Method to change cuurent screen
     * @param newState
     */
    public void setState(GUIStates newState){
        this.state = newState;
        draw();
    }

    /**
     * Method to show info dialog window to user
     * @param message
     */
    public void showInfoMessage(LocalizedMessage message){
        JOptionPane.showMessageDialog(mainFrame,
                message.getMessage(ResourceBundlesController.getInstance().getMessagesBundle()),
                "", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method to show error dialog window to user
     * @param e
     */
    public void showErrorMessage(LocalizedException e){
        JOptionPane.showMessageDialog(mainFrame,
                e.getMessage(ResourceBundlesController.getInstance().getExceptionsBundle()),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method to show warning dialog window to user
     * @param message
     */
    public void showWarningMessage(LocalizedMessage message){
        JOptionPane.showMessageDialog(mainFrame,
                message.getMessage(ResourceBundlesController.getInstance().getMessagesBundle()),
                "Attention", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Method to handle response from server
     * <p>It shows message or error as dialog window
     * @param response
     */
    public void handleServerResponse(ServerResponse response) {
        switch (response.state()) {
            case SUCCESS:
                showInfoMessage((LocalizedMessage) response.data());
                break;
            case EXCEPTION:
                showErrorMessage((LocalizedException) response.data());
        }
    }
}