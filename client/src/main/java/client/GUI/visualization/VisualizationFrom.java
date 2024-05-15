package client.GUI.visualization;

import client.Controllers.CollectionController;
import client.GUI.MainForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VisualizationFrom extends JFrame {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;
    private JPanel contentPane;
    private CoordinatesNet coordinatesNet;
    public VisualizationFrom(){
        setUpUI();
        setContentPane(contentPane);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MainForm.getInstance().resetVisualizationMode();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.isControlDown() && e.getKeyCode() == 68){
                    coordinatesNet.setDefaultView();
                }
            }
        });
    }

    public void update(){
        coordinatesNet.updateCollection(CollectionController.getInstance().getCollection());
    }

    private void setUpUI(){
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        coordinatesNet = new CoordinatesNet();
        coordinatesNet.setLayout(null);
        contentPane.add(coordinatesNet, BorderLayout.CENTER);
    }
}
