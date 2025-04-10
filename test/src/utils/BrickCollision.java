package utils;

import entities.Ball;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class BrickCollision {

    private final BricksManager map;
    private final List<Ball> balls;
    private final List<Bonus> bonuses;
    private final Random rand;

    public BrickCollision(BricksManager map, List<Ball> balls, List<Bonus> bonuses) {
        this.map = map;
        this.balls = balls;
        this.bonuses = bonuses;
        this.rand = new Random();
    }

    public void checkCollisions() {
        for (Ball b : balls) {
            Rectangle ballRect = new Rectangle(b.getX(), b.getY(), b.getDiameter(), b.getDiameter()); // Rectangle de la balle

            // Vérifie les collisions avec les briques
            outerLoop:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.getBrickValue(i, j) > 0) { // Si la brique existe (valeur > 0)
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            int brickType = map.getBrickValue(i, j);
                            map.hitBrick(i, j); // Détruire ou abîmer la brique
                            if (brickType == 3 && rand.nextBoolean()) {
                                bonuses.add(new Bonus(brickX + map.brickWidth / 2, brickY, rand.nextInt(3) + 1)); // Ajouter un bonus
                            }

                            // Mettre à jour le score
                            int scoreIncrease = (brickType == 2 ? 10 : 5);
                            // La logique pour calculer le score doit être intégrée ici

                            // Gestion du rebond de la balle après collision avec la brique
                            int ballSize = 20;
                            Rectangle prevBallRect = new Rectangle(b.getX(), b.getY(), b.getDiameter(), b.getDiameter());
                            boolean fromLeft = prevBallRect.x + ballSize <= brickRect.x;
                            boolean fromRight = prevBallRect.x >= brickRect.x + brickRect.width;

                            // Collision horizontale ou verticale avec la brique
                            if ((fromLeft && b.getDirX() > 0) || (fromRight && b.getDirX() < 0)) {
                                b.setDirX(-b.getDirX());
                            } else {
                                b.setDirY(-b.getDirY());
                            }

                            break outerLoop; // Sortir des boucles de brique et de balles
                        }
                    }
                }
            }
        }
    }
}
