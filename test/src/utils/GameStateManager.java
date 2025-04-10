package utils;

import java.util.prefs.Preferences;

public class GameStateManager {
    private GameState gameState;
    private int score;
    private int bestScore;
    private int lives;

    public GameStateManager() {
        gameState = GameState.MENU;
        score = 0;
        bestScore = loadBestScore();
        lives = 3;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setScore(int score) {
        this.score = score;
        updateBestScore(score);
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLives() {
        lives--;
    }

    public void resetLives() {
        lives = 3;
    }

    public void updateBestScore(int score) {
        if (score > bestScore) {
            bestScore = score;
            saveBestScore();
        }
    }

    private void saveBestScore() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        prefs.putInt("bestScore", bestScore);
    }

    private int loadBestScore() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        return prefs.getInt("bestScore", 0);
    }

    public void resetGame() {
        gameState = GameState.PLAYING;
        score = 0;
        resetLives();
    }

    public void nextWave() {
        gameState = GameState.PLAYING;
    }

    public void gameOver() {
        gameState = GameState.GAMEOVER;
    }

    public void pause() {
        gameState = GameState.PAUSED;
    }

    public void resume() {
        gameState = GameState.PLAYING;
    }

    public enum GameState {
        MENU, PLAYING, PAUSED, GAMEOVER
    }
}
