package client.GUI.visualization;

import client.GUI.MainForm;
import common.Collection.Worker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

public class WorkerImage extends JComponent {
    private static final int WORKER_RADIUS = 24;
    private static final int HEAD_RADIUS = 8;
    private static final int BODY_RADIUS = 16;
    private static final Color WORKER_COLOR = Color.BLACK;
    private static final int MAX_HEAD_HEIGHT = 5;
    private static final int MIN_HEAD_HEIGHT = 0;
    private static final int ANIMATION_PERIOD = 100;
    private static final double MAX_SCALE = 3;
    private static final double MIN_SCALE = 0.5;

    private int headHeight = 0;
    private int animationInc = 1;

    private final Color backgroundColor;
    private final Worker worker;
    private final double scale;

    public WorkerImage(Worker worker, Color color, double scale){
        this.worker = worker;
        this.backgroundColor = color;
        this.scale = Math.max(scale * MAX_SCALE, MIN_SCALE);
        init();
    }

    private void init(){
        setSize((int) Math.round(WORKER_RADIUS*2*scale), (int) Math.round(WORKER_RADIUS*2*scale));

        new Timer("Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                change();
                repaint();
            }
        }, 0, ANIMATION_PERIOD);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    MainForm.getInstance().updateWorker(worker);
                }
            }
        });

        setToolTipText("<html>" + worker.toString().replace("\n", "<br>").replace("\t", "&emsp;") + "</html>");
    }

    private void change(){
        headHeight = (headHeight + animationInc);
        if(headHeight == MAX_HEAD_HEIGHT || headHeight == MIN_HEAD_HEIGHT) animationInc *= -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scale, scale);
        g2d.setColor(backgroundColor);
        g2d.fillOval(0, 0, WORKER_RADIUS * 2, WORKER_RADIUS * 2);
        g2d.setColor(WORKER_COLOR);
        g2d.fillOval(WORKER_RADIUS - HEAD_RADIUS, WORKER_RADIUS - HEAD_RADIUS * 2 - headHeight, HEAD_RADIUS * 2, HEAD_RADIUS * 2);
        g2d.fillArc(WORKER_RADIUS - BODY_RADIUS, WORKER_RADIUS, BODY_RADIUS * 2, BODY_RADIUS * 2, 0, 180);
    }
}
