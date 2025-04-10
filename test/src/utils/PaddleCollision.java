package utils;

import entities.Ball;
import entities.Paddle;

import java.awt.*;

public class PaddleCollision {

    public static void checkPaddleCollision(Ball ball, Paddle paddle) {
        Rectangle ballBounds = Ball.getBounds();
        Rectangle paddleBounds = Paddle.getBounds();

        if (ballBounds.intersects(paddleBounds)) {
            handlePaddleCollision(ball, paddle);
        }
    }

    private static void handlePaddleCollision(Ball ball, Paddle paddle) {
        Rectangle paddleBounds = Paddle.getBounds();
        Rectangle ballBounds = Ball.getBounds();

        Rectangle left = getLeftSideBounds(paddleBounds);
        Rectangle right = getRightSideBounds(paddleBounds);
        Rectangle top = getTopSideBounds(paddleBounds);

        if (ballBounds.intersects(left)) {
            ball.setDirX(-Math.abs(ball.getDirX()));
        } else if (ballBounds.intersects(right)) {
            ball.setDirX(Math.abs(ball.getDirX()));
        }

        if (ballBounds.intersects(top)) {
            calculateBounceAngle(ball, paddle);
        }
    }

    private static Rectangle getLeftSideBounds(Rectangle paddleBounds) {
        int paddleX = paddleBounds.x;
        int paddleY = paddleBounds.y;
        int paddleHeight = paddleBounds.height;
        return new Rectangle(paddleX, paddleY, 5, paddleHeight);
    }

    private static Rectangle getRightSideBounds(Rectangle paddleBounds) {
        int paddleX = paddleBounds.x;
        int paddleY = paddleBounds.y;
        int paddleWidth = paddleBounds.width;
        int paddleHeight = paddleBounds.height;
        return new Rectangle(paddleX + paddleWidth - 5, paddleY, 5, paddleHeight);
    }

    private static Rectangle getTopSideBounds(Rectangle paddleBounds) {
        int paddleX = paddleBounds.x;
        int paddleY = paddleBounds.y;
        int paddleWidth = paddleBounds.width;
        return new Rectangle(paddleX, paddleY, paddleWidth, 5);
    }

    private static void calculateBounceAngle(Ball ball, Paddle paddle) {
        double speed = Math.sqrt(ball.getDirX() * ball.getDirX() + ball.getDirY() * ball.getDirY());
        double hitRatio = (ball.getX() + ball.getDiameter() / 2.0 - paddle.getX()) / paddle.getWidth();
        double angle = Math.toRadians(150 - 120 * hitRatio);

        int newDirX = (int) Math.round(speed * Math.cos(angle));
        int newDirY = (int) -Math.abs(Math.round(speed * Math.sin(angle)));

        ball.setDirX(newDirX);
        ball.setDirY(newDirY);
    }
}
