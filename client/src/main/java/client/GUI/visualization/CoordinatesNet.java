package client.GUI.visualization;

import common.Collection.Worker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

public class CoordinatesNet extends JComponent {
    private static final int LEFT_OFFSET = 50;
    private static final int TOP_OFFSET = 20;
    private static final int BOTTOM_OFFSET = 30;
    private static final int RIGHT_OFFSET = 30;
    private static final int CELL_SIZE = 20;
    private static final int X_TEXT_OFFSET = 20;
    private static final int Y_TEXT_OFFSET = 5;
    private static final int SCALE_TEXT_X_OFFSET = 150;
    private static final int SCALE_TEXT_Y_OFFSET = 20;
    private static final int CELL_UNIT = 4;
    private static final int HALF_DASH_LEN = 5;
    private static final double SCALE_STEP = 0.05;

    private final Font coordinatesFont = new Font("Arial", Font.PLAIN, 16);
    private final Font infoFont = new Font("Arial", Font.BOLD, 20);

    private double scale = 1.0;
    private int X0 = 0;
    private int Y0 = 0;

    private boolean isDragging = false;
    private int dragX0;
    private int dragY0;
    private int tmpX;
    private int tmpY;

    private Collection<Worker> collection;

    /**
     * Method to update collection of components on coordinates net
     * <p>After update they are automatically redrawn
     * @param collection
     */
    public void updateCollection(Collection<Worker> collection){
        this.collection = collection;
        redraw();
    }

    /**
     * Method to set the default view of coordinates net
     * <p>It sets scale and shifts of coordinates system to initial values
     */
    public void setDefaultView(){
        scale = 1;
        X0 = 0;
        Y0 = 0;
        redraw();
    }

    public CoordinatesNet(){
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                tmpX = X0 - (int) Math.round(((e.getX() - dragX0)) * scale);
                tmpY = Y0 + (int) Math.round((e.getY() - dragY0) * scale);
                redraw();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1) return;
                if(!isDragging){
                    isDragging = true;
                    dragX0 = e.getX();
                    dragY0 = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1) return;
                isDragging = false;
                X0 = tmpX;
                Y0 = tmpY;
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scale += (e.getWheelRotation()) * SCALE_STEP;
                scale = Math.max(scale, SCALE_STEP);
                redraw();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redraw();
            }
        });
    }

    /**
     * Main painting method
     * <p>It draws coordinates net, coordinates values and shifts
     * @param g the <code>Graphics</code> object to protect
     */
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

        for(int x = LEFT_OFFSET + CELL_SIZE * CELL_UNIT; x <= endX; x += CELL_SIZE * CELL_UNIT){
            g2d.drawLine(x, endY - HALF_DASH_LEN, x, endY + HALF_DASH_LEN);
        }
        for(int y = endY - CELL_SIZE * CELL_UNIT; y >= TOP_OFFSET; y -= CELL_SIZE * CELL_UNIT){
            g2d.drawLine(LEFT_OFFSET - HALF_DASH_LEN, y, LEFT_OFFSET + HALF_DASH_LEN, y);
        }

        int curX = (isDragging ? tmpX : X0);
        g2d.setFont(coordinatesFont);
        for(int x = LEFT_OFFSET; x <= endX; x += CELL_SIZE * CELL_UNIT){
            g2d.drawString(String.valueOf(curX), x, endY + X_TEXT_OFFSET);
            curX += (int) Math.round((CELL_SIZE * CELL_UNIT) * scale);
        }
        int curY = (isDragging ? tmpY : Y0);
        for(int y = endY; y >= TOP_OFFSET; y -= CELL_SIZE * CELL_UNIT){
            g2d.drawString(String.valueOf(curY), Y_TEXT_OFFSET, y);
            curY += (int) Math.round((CELL_SIZE * CELL_UNIT) * scale);
        }

        g2d.setFont(infoFont);
        g2d.drawString(String.format("Scale: %d%%", (int) Math.round((1 / scale) * 100)), getWidth() - SCALE_TEXT_X_OFFSET, SCALE_TEXT_Y_OFFSET);
    }

    /**
     * Method to redraw all components from collection
     */
    private void redraw(){
        removeAll();
        collection.forEach(worker -> {
            WorkerImage workerImage = new WorkerImage(worker, Color.BLUE);
            addComponent(workerImage, worker.getCoordinates().getX(), worker.getCoordinates().getY());
        });
        repaint();
    }

    /**
     * Method to place component into given coordinates
     * <p>It calculates the real screen position of component, taking into account current scale and shift of coordinate system
     * <p>If component won't be visible on screen, it is not placed
     * @param component Component to add
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void addComponent(JComponent component, double x, double y){
        int x0 = LEFT_OFFSET;
        int y0 = getHeight() - BOTTOM_OFFSET;

        int nx = x0 - component.getWidth() / 2 + (int) Math.round((x - (isDragging ? tmpX : X0)) / scale);
        int ny = y0 - component.getHeight() / 2 - (int) Math.round((y - (isDragging ? tmpY : Y0)) / scale);

        if(nx < x0 - component.getWidth() / 2 || nx >= getWidth() || ny > y0 - component.getHeight() / 2 || ny >= getHeight()) return;

        add(component);
        component.setLocation(nx, ny);
    }
}
