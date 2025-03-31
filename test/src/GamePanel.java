// GamePanel.java
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener {
    enum GameState { MENU, PLAYING, PAUSED, GAMEOVER }
    private GameState gameState = GameState.MENU;

    private Timer timer;
    private int delay = 8;
    private int ballX = 120, ballY = 350, ballDirX = -1, ballDirY = -2;
    private int paddleX = 310;
    private final int paddleWidth = 100;
    private final int paddleHeight = 10;
    private boolean moveLeft = false, moveRight = false;
    private BrickMap map;
    private int score = 0;
    private int totalBricks;

    public GamePanel() {
        this.setPreferredSize(new Dimension(700, 600));
        this.setBackground(Color.black);
        map = new BrickMap(3, 7);
        totalBricks = 3 * 7;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
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
            // background
            g.setColor(Color.black);
            g.fillRect(1, 1, 692, 592);

            // bricks
            map.draw((Graphics2D) g);

            // score
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Score: " + score, 550, 30);

            // paddle
            g.setColor(Color.green);
            g.fillRect(paddleX, 550, paddleWidth, paddleHeight);

            // ball
            g.setColor(Color.yellow);
            g.fillOval(ballX, ballY, 20, 20);

            // pause button
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
            ballX += ballDirX;
            ballY += ballDirY;

            if (ballX < 0 || ballX > 670) ballDirX = -ballDirX;
            if (ballY < 0) ballDirY = -ballDirY;

            if (ballY > 570) gameState = GameState.GAMEOVER;

            Rectangle paddleRect = new Rectangle(paddleX, 550, paddleWidth, paddleHeight);
            Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);

            if (ballRect.intersects(paddleRect)) ballDirY = -ballDirY;

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            map.map[i][j] = 0;
                            totalBricks--;
                            score += 5;
                            playSound("sounds/brick_hit.wav");

                            if (ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }
                    }
                }
            }

            if (totalBricks <= 0) {
                gameState = GameState.GAMEOVER;
            }

            if (moveRight && paddleX < 600) paddleX += 5;
            if (moveLeft && paddleX > 10) paddleX -= 5;
        }
        repaint();
    }

    private void playSound(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resetGame() {
        paddleX = 310;
        ballX = 120;
        ballY = 350;
        ballDirX = -1;
        ballDirY = -2;
        score = 0;
        map = new BrickMap(3, 7);
        totalBricks = 3 * 7;
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

    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (gameState == GameState.MENU) {
            if (mx >= 270 && mx <= 430 && my >= 300 && my <= 340) {
                resetGame();
            }
        } else if (gameState == GameState.PLAYING) {
            if (mx >= 640 && mx <= 690 && my >= 10 && my <= 40) {
                gameState = GameState.PAUSED;
            }
        } else if (gameState == GameState.PAUSED) {
            if (mx >= 250 && mx <= 450 && my >= 270 && my <= 310) {
                gameState = GameState.PLAYING;
            } else if (mx >= 250 && mx <= 450 && my >= 330 && my <= 370) {
                resetGame();
            }
        } else if (gameState == GameState.GAMEOVER) {
            if (mx >= 250 && mx <= 450 && my >= 350 && my <= 390) {
                resetGame();
            }
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}