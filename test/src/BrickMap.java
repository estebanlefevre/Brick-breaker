// BrickMap.java
import java.awt.*;

public class BrickMap {
    public int[][] map;
    public int brickWidth;
    public int brickHeight;

    public BrickMap(int row, int col) {
        map = new int[row][col];
        for (int[] rowArr : map)
            java.util.Arrays.fill(rowArr, 1);

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(2));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
}
