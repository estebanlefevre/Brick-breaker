package utils;

import entities.Brick;

import java.awt.*;
import java.util.Random;

public class BricksManager {
    private final Brick[][] bricks;
    final int brickWidth;
    final int brickHeight;

    public BricksManager(int rows, int cols) {
        bricks = new Brick[rows][cols];
        brickWidth = 540 / cols;
        brickHeight = 150 / rows;
        initializeBricks(rows, cols);
    }

    private void initializeBricks(int rows, int cols) {
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int type = rand.nextInt(100);
                Color color = generateRandomColor(rand);
                bricks[i][j] = new Brick(assignBrickType(type), color);
            }
        }
    }

    private int assignBrickType(int type) {
        if (type < 70) return 1;   // Brique normale
        else if (type < 90) return 2; // Brique solide
        else return 3; // Brique spéciale
    }

    private Color generateRandomColor(Random rand) {
        float hue = rand.nextFloat();
        float saturation = 0.6f + rand.nextFloat() * 0.4f;
        float brightness = 0.7f + rand.nextFloat() * 0.3f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public Brick getBrick(int row, int col) {
        return bricks[row][col];
    }

    public void hitBrick(int row, int col) {
        bricks[row][col].hit();
    }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick brick = bricks[i][j];
                if (brick.getType() > 0) {
                    int x = j * brickWidth + 80;
                    int y = i * brickHeight + 50;
                    brick.draw(g, x, y, brickWidth, brickHeight);
                }
            }
        }
    }
}
