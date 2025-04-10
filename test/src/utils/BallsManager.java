package utils;

import entities.Ball;
import entities.Paddle;

import java.util.ArrayList;

public class BallsManager {

    private final ArrayList<Ball> balls = new ArrayList<>();

    private void resetBall() {
        int paddleX = Paddle.getX();
        int paddleWidth = Paddle.getWidth();
        Ball nouvelleBalle = new Ball(paddleX + paddleWidth / 2, 480, 2, -2, 20);
        balls.add(new Ball(paddleX + paddleWidth / 2, 480, 2, -2, 20));
    }
}
