package inputs;

import java.awt.event.KeyEvent;

public class KeyboardInputs {

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
    }

    public void keyTyped(KeyEvent e) {}

}
