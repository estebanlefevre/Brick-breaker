// BrickMap.java
import java.awt.*;
import java.util.Random;

public class BrickMap {
    public int[][] map;
    public int brickWidth;
    public int brickHeight;
    private Color[][] brickColors;

    public BrickMap(int row, int col) {
        map = new int[row][col];
        brickColors = new Color[row][col];
        Random rand = new Random();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int type = rand.nextInt(100);
                if (type < 70) {
                    map[i][j] = 1; // normale
                } else if (type < 90) {
                    map[i][j] = 2; // solide (2 coups)
                } else {
                    map[i][j] = 3; // spéciale (bonus/malus ou multi-balle)
                }

                float hue = rand.nextFloat();
                float saturation = 0.6f + rand.nextFloat() * 0.4f;
                float brightness = 0.7f + rand.nextFloat() * 0.3f;
                brickColors[i][j] = Color.getHSBColor(hue, saturation, brightness);
            }
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public int getBrickValue(int row, int col) {
        return map[row][col];
    }

    public void hitBrick(int row, int col) {
        if (map[row][col] == 2) {
            map[row][col] = 1; // solide devient normale
        } else {
            map[row][col] = 0; // détruite
        }
    }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    int x = j * brickWidth + 80;
                    int y = i * brickHeight + 50;

                    g.setColor(brickColors[i][j]);
                    g.fillRect(x, y, brickWidth, brickHeight);

                    g.setColor(Color.black);
                    g.setStroke(new BasicStroke(2));
                    g.drawRect(x, y, brickWidth, brickHeight);

                    g.setColor(Color.white);
                    g.drawString(String.valueOf(map[i][j]), x + brickWidth / 2 - 4, y + brickHeight / 2 + 4);
                }
            }
        }
    }
}
