package inputs;

import Main.GamePanel;

import java.awt.event.MouseEvent;

public class MouseInputs {

    public void mouseMoved(MouseEvent e) {
        paddleX = e.getX() - paddleWidth / 2;
        if (paddleX < 0) paddleX = 0;
        if (paddleX > getWidth() - paddleWidth) paddleX = getWidth() - paddleWidth;
    }

    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (gameState == GamePanel.GameState.MENU) {
            if (mx >= 270 && mx <= 430 && my >= 300 && my <= 340) resetGame();
        } else if (gameState == GamePanel.GameState.PLAYING) {
            if (mx >= 640 && mx <= 690 && my >= 10 && my <= 40) gameState = GamePanel.GameState.PAUSED;
        } else if (gameState == GamePanel.GameState.PAUSED) {
            if (mx >= 250 && mx <= 450 && my >= 270 && my <= 310) gameState = GamePanel.GameState.PLAYING;
            else if (mx >= 250 && mx <= 450 && my >= 330 && my <= 370) resetGame();
        } else if (gameState == GamePanel.GameState.GAMEOVER) {
            if (mx >= 250 && mx <= 450 && my >= 350 && my <= 390) resetGame();
        }
    }

    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
