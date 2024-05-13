package client.GUI;

import client.Controllers.CollectionController;
import common.Collection.Worker;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

public class VisualizationDialog extends JDialog {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;
    private JPanel contentPane;
    private CoordinatesNet coordinatesNet;
    public VisualizationDialog(){
        setUpUI();
        setContentPane(contentPane);
        setModal(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MainForm.getInstance().resetVisualizationMode();
            }
        });
    }

    public void update(){
        coordinatesNet.removeAll();
        CollectionController.getInstance().getCollection().forEach(worker ->
                coordinatesNet.addComponent(
                        new WorkerImage(),
                        (int) Math.round(worker.getCoordinates().getX()),
                        (int) Math.round(worker.getCoordinates().getY())));
        coordinatesNet.repaint();
    }

    private class CoordinatesNet extends JComponent{
        private static final int LEFT_OFFSET = 20;
        private static final int TOP_OFFSET = 20;
        private static final int BOTTOM_OFFSET = 50;
        private static final int RIGHT_OFFSET = 30;
        private static final int CELL_SIZE = 20;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            int endX = getWidth() - RIGHT_OFFSET;
            int endY = getHeight() - BOTTOM_OFFSET;

            for(int x = LEFT_OFFSET + CELL_SIZE; x < endX; x += CELL_SIZE){
                g2d.drawLine(x, TOP_OFFSET, x, endY);
            }
            for(int y = endY - CELL_SIZE; y > TOP_OFFSET; y -= CELL_SIZE){
                g2d.drawLine(LEFT_OFFSET, y, endX,y);
            }

            g2d.setStroke(new BasicStroke(3f));
            g2d.drawLine(LEFT_OFFSET, TOP_OFFSET, LEFT_OFFSET, endY);
            g2d.drawLine(LEFT_OFFSET, endY, endX, endY);
        }

        public void addComponent(JComponent component, int x, int y){
            int x0 = LEFT_OFFSET;
            int y0 = getHeight() - BOTTOM_OFFSET;
            add(component);
            component.setLocation(x0 + x - component.getWidth()/2, y0 - y - component.getHeight()/2);
        }
    }

    private class WorkerImage extends JComponent{
        private static final int WORKER_RADIUS = 24;
        private static final int HEAD_RADIUS = 8;
        private static final int BODY_RADIUS = 16;
        private int headHeight = 0;
        private int animationInc = 1;

        public WorkerImage(){
            setSize(WORKER_RADIUS*2, WORKER_RADIUS*2);
            new Timer(100, e -> {
                change();
                repaint();
            }).start();
        }

        public void change(){
            headHeight = (headHeight + animationInc);
            if(headHeight == 5 || headHeight == 0) animationInc *= -1;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(Color.BLUE);
            g2d.fillOval(0, 0, WORKER_RADIUS*2, WORKER_RADIUS*2);
            g2d.setColor(Color.BLACK);
            g2d.fillOval(WORKER_RADIUS - HEAD_RADIUS, WORKER_RADIUS - HEAD_RADIUS*2 - headHeight, HEAD_RADIUS*2, HEAD_RADIUS*2);
            g2d.fillArc(WORKER_RADIUS - BODY_RADIUS, WORKER_RADIUS, BODY_RADIUS*2, BODY_RADIUS*2, 0, 180);
        }
    }

    private void setUpUI(){
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        coordinatesNet = new CoordinatesNet();
        coordinatesNet.setLayout(null);
        contentPane.add(coordinatesNet, BorderLayout.CENTER);
    }
}
