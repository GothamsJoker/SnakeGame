package org.gaming;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class ScreenPanel extends JPanel implements ActionListener {

    private final int SCREEN_WIDTH = 300;
    private final int SCREEN_HEIGHT = 300;
    private final int DOT_ICON_SIZE = 10;
    private final int DOT_ICONS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[DOT_ICONS];
    private final int y[] = new int[DOT_ICONS];

    private int parts;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inSnakeGame = true;

    private Timer timer;
    private Image snakepart;
    private Image apple;
    private Image snakehead;


    public ScreenPanel() {

        initScreenPanel();
    }

    private void initScreenPanel() {
        addKeyListener(new GameKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        loadIcons();
        initSnakeGame();
    }

    private void loadIcons() {

        URL url = getClass().getResource("apple.png");
        Image iisnakepart = new ImageIcon(url).getImage();
        snakepart = iisnakepart;

        URL url1 = getClass().getResource("dot.png");
        Image iiapple = new ImageIcon(url1).getImage();
        apple = iiapple;

        URL url2 = getClass().getResource("head.png");
        Image iisnakehead = new ImageIcon(url2).getImage();
        snakehead = iisnakehead;

    }

    private void initSnakeGame() {

        parts = 3;

        for (int z = 0; z < parts; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inSnakeGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < parts; z++) {
                if (z == 0) {
                    g.drawImage(snakehead, x[z], y[z], this);
                } else {
                    g.drawImage(snakepart, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "You Died";
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.RED);
        g.setFont(small);
        g.drawString(msg, (SCREEN_WIDTH - metr.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2);
    }
    private void findAppleIcon() {

        if ((x[0] == apple_x) && (y[0] == apple_y)){
            parts++;
            locateApple();
        }
    }

    private void shift() {
        for (int z = parts; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= DOT_ICON_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_ICON_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_ICON_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_ICON_SIZE;
        }
    }


    private void findCollision() {
        for (int z = parts; z > 0; z--) {
            if ((z > 6 ) && (x[0] == x[z]) && (y[0] == y[z])) {
                inSnakeGame = false;
            }
        }
        if (y[0] >= SCREEN_HEIGHT) {
            inSnakeGame = false;
        }
        if (y[0] < 0){
            inSnakeGame = false;
        }
        if (x[0] >= SCREEN_WIDTH) {
            inSnakeGame = false;
        }
        if (x[0] < 0) {
            inSnakeGame = false;
        }
        if (!inSnakeGame) {
            timer.stop();
        }
    }
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_ICON_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_ICON_SIZE));
    }
    @Override
    public void actionPerformed(ActionEvent event) {

        if (inSnakeGame) {
            findAppleIcon();
            findCollision();
            shift();
        }

        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
