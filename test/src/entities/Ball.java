package entities;

import javax.swing.*;
import java.awt.*;

public class Ball {
    private static int x;
    private static int y;
    private int dirX;
    private int dirY;
    private static int diameter;

    public Ball(int x, int y, int dx, int dy, int diameter) {
        Ball.x = x;
        Ball.y = y;
        this.dirX = dx;
        this.dirY = dy;
        Ball.diameter = diameter;
    }

    public static Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void paint(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(x, y, 20, 20);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDirX() { return dirX; }
    public int getDirY() { return dirY; }
    public int getDiameter() { return diameter; }

    public void setDirX(int dirX) { this.dirX = dirX; }
    public void setDirY(int dirY) { this.dirY = dirY; }
}
