import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    enum GameState { MENU, PLAYING, PAUSED, GAMEOVER }
    private GameState gameState = GameState.MENU;

    private javax.swing.Timer timer;
    private int delay = 8;

    private ArrayList<Ball> balls = new ArrayList<>();
    private int paddleX = 310;
    private int paddleWidth = 100;
    private final int paddleHeight = 10;

    private boolean moveLeft = false, moveRight = false;
    private BrickMap map;
    private int score = 0;
    private int totalBricks;
    private ArrayList<Bonus> bonuses = new ArrayList<>();
    private Random rand = new Random();

    private long paddleEffectEndTime = 0;

    // === Bonus ===
    private class Bonus {
        int x, y;
        int width = 20, height = 20;
        int type; // 1 = extra ball, 2 = shrink, 3 = enlarge
        public Bonus(int x, int y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    // === Ball ===
    private class Ball {
        int x, y, dirX, dirY;
        public Ball(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dirX = dx;
            this.dirY = dy;
        }
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(700, 600));
        this.setBackground(Color.black);
        map = new BrickMap(8, 14);
        totalBricks = countInitialBricks();
        balls.add(new Ball(120, 350, -1, -2));
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    private int countInitialBricks() {
        int count = 0;
        for (int[] row : map.map) {
            for (int val : row) {
                if (val > 0) count++;
            }
        }
        return count;
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (gameState == GameState.MENU) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Brick Breaker", 240, 200);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawRect(270, 300, 160, 40);
            g.drawString("Démarrer", 295, 330);
        } else if (gameState == GameState.PLAYING || gameState == GameState.PAUSED) {
            g.setColor(Color.black);
            g.fillRect(1, 1, 692, 592);

            map.draw((Graphics2D) g);
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Score: " + score, 550, 30);

            g.setColor(Color.green);
            g.fillRect(paddleX, 550, paddleWidth, paddleHeight);

            for (Ball b : balls) {
                g.setColor(Color.yellow);
                g.fillOval(b.x, b.y, 20, 20);
            }

            for (Bonus b : bonuses) {
                switch (b.type) {
                    case 1 -> g.setColor(Color.cyan);
                    case 2 -> g.setColor(Color.red);
                    case 3 -> g.setColor(Color.green);
                }
                g.fillOval(b.x, b.y, b.width, b.height);
            }

            g.setColor(Color.gray);
            g.fillRect(640, 10, 50, 30);
            g.setColor(Color.white);
            g.drawString("Pause", 645, 32);

            if (gameState == GameState.PAUSED) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, 700, 600);
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pause", 300, 200);
                g.drawRect(250, 270, 200, 40);
                g.drawString("Reprendre", 270, 300);
                g.drawRect(250, 330, 200, 40);
                g.drawString("Redémarrer", 260, 360);
            }
        } else if (gameState == GameState.GAMEOVER) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);
            g.drawRect(250, 350, 200, 40);
            g.drawString("Redémarrer", 260, 380);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.PLAYING) {
            Rectangle paddleRect = new Rectangle(paddleX, 550, paddleWidth, paddleHeight);

            if (paddleEffectEndTime > 0 && System.currentTimeMillis() > paddleEffectEndTime) {
                paddleWidth = 100;
                paddleEffectEndTime = 0;
            }

            Iterator<Ball> ballIterator = balls.iterator();
            while (ballIterator.hasNext()) {
                Ball b = ballIterator.next();
                b.x += b.dirX;
                b.y += b.dirY;

                if (b.x < 0 || b.x > 670) b.dirX = -b.dirX;
                if (b.y < 0) b.dirY = -b.dirY;
                if (b.y > 570) {
                    ballIterator.remove();
                    continue;
                }

                Rectangle ballRect = new Rectangle(b.x, b.y, 20, 20);
                if (ballRect.intersects(paddleRect)) b.dirY = -b.dirY;

                A: for (int i = 0; i < map.map.length; i++) {
                    for (int j = 0; j < map.map[0].length; j++) {
                        if (map.getBrickValue(i, j) > 0) {
                            int brickX = j * map.brickWidth + 80;
                            int brickY = i * map.brickHeight + 50;
                            Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);

                            if (ballRect.intersects(brickRect)) {
                                int brickType = map.getBrickValue(i, j);
                                map.hitBrick(i, j);
                                if (brickType == 3 && rand.nextBoolean()) {
                                    bonuses.add(new Bonus(brickX + map.brickWidth / 2, brickY, rand.nextInt(3) + 1));
                                }
                                score += (brickType == 2 ? 10 : 5);
                                totalBricks--;

                                if (b.x + 19 <= brickRect.x || b.x + 1 >= brickRect.x + brickRect.width) {
                                    b.dirX = -b.dirX;
                                } else {
                                    b.dirY = -b.dirY;
                                }
                                break A;
                            }
                        }
                    }
                }
            }

            if (balls.isEmpty()) gameState = GameState.GAMEOVER;

            Iterator<Bonus> it = bonuses.iterator();
            while (it.hasNext()) {
                Bonus b = it.next();
                b.y += 3;
                if (new Rectangle(b.x, b.y, b.width, b.height).intersects(paddleRect)) {
                    switch (b.type) {
                        case 2 -> {
                            paddleWidth = Math.max(40, paddleWidth - 40);
                            paddleEffectEndTime = System.currentTimeMillis() + 5000;
                        }
                        case 3 -> {
                            paddleWidth = Math.min(200, paddleWidth + 40);
                            paddleEffectEndTime = System.currentTimeMillis() + 5000;
                        }
                        case 1 -> balls.add(new Ball(paddleX + paddleWidth / 2, 540, rand.nextBoolean() ? -2 : 2, -2));
                    }
                    it.remove();
                } else if (b.y > 600) {
                    it.remove();
                }
            }

            if (totalBricks <= 0) gameState = GameState.GAMEOVER;
            if (moveRight && paddleX < 700 - paddleWidth) paddleX += 5;
            if (moveLeft && paddleX > 10) paddleX -= 5;
        }
        repaint();
    }

    public void resetGame() {
        paddleX = 310;
        paddleWidth = 100;
        score = 0;
        paddleEffectEndTime = 0;
        map = new BrickMap(8, 14);
        totalBricks = countInitialBricks();
        bonuses.clear();
        balls.clear();
        balls.add(new Ball(120, 350, -1, -2));
        gameState = GameState.PLAYING;
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
    }

    public void keyTyped(KeyEvent e) {}
    public void mouseMoved(MouseEvent e) {
        paddleX = e.getX() - paddleWidth / 2;
        if (paddleX < 0) paddleX = 0;
        if (paddleX > getWidth() - paddleWidth) paddleX = getWidth() - paddleWidth;
    }

    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (gameState == GameState.MENU) {
            if (mx >= 270 && mx <= 430 && my >= 300 && my <= 340) resetGame();
        } else if (gameState == GameState.PLAYING) {
            if (mx >= 640 && mx <= 690 && my >= 10 && my <= 40) gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED) {
            if (mx >= 250 && mx <= 450 && my >= 270 && my <= 310) gameState = GameState.PLAYING;
            else if (mx >= 250 && mx <= 450 && my >= 330 && my <= 370) resetGame();
        } else if (gameState == GameState.GAMEOVER) {
            if (mx >= 250 && mx <= 450 && my >= 350 && my <= 390) resetGame();
        }
    }

    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
