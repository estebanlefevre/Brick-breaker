package entities;

import java.awt.*;

public class Paddle {
    private static final int paddleX = 310;
    private static final int paddleY = 550;
    private static final int paddleWidth = 100;
    private static final int paddleHeight = 10;

    private long paddleEffectEndTime = 0;

    public void paint(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(paddleX, 550, paddleWidth, paddleHeight);
    }

    public static Rectangle getBounds() {
        return new Rectangle(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    public static int getX() { return paddleX; }
    public int getY() { return paddleY; }
    public static int getWidth() { return paddleWidth; }
    public int getHeight() { return paddleHeight; }
}
