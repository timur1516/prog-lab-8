package client.GUI;

import common.net.requests.ServerResponse;

import javax.swing.*;

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

    private GUIController(){
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);

        state = GUIStates.LOG_IN;

        draw();
    }

    private void draw(){
        JComponent contentPanel = switch (state){
            case MAIN -> MainForm.getInstance().$$$getRootComponent$$$();
            case LOG_IN -> (new LogInForm()).$$$getRootComponent$$$();
            case SIGN_UP -> (new SignUpForm().$$$getRootComponent$$$());
        };
        mainFrame.setContentPane(contentPanel);
        mainFrame.setVisible(true);
    }

    public void setState(GUIStates newState){
        this.state = newState;
        draw();
    }

    public void showInfoMessage(String message){
        JOptionPane.showMessageDialog(mainFrame, message, "", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(Exception e){
        showErrorMessage(e.getMessage());
    }

    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String message){
        JOptionPane.showMessageDialog(mainFrame, message, "", JOptionPane.WARNING_MESSAGE);
    }

    public void handleServerResponse(ServerResponse response) {
        switch (response.state()) {
            case SUCCESS:
                showInfoMessage(response.data().toString());
                break;
            case EXCEPTION:
                showErrorMessage((Exception) response.data());
        }
    }
}