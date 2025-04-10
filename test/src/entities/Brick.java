package entities;

import java.awt.*;

public class Brick {
    private int type;
    private Color color;

    public Brick(int type, Color color) {
        this.type = type;
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);

        g.setColor(Color.white);
        g.drawString(String.valueOf(type), x + width / 2 - 4, y + height / 2 + 4);
    }

    public void hit() {
        if (type >= 2) type--; // Si c'est une brique solide, elle devient normale
        else type = 0; // Sinon, elle est détruite
    }
}
