package com.comp2042.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Random;

public class Coin {
    private Point pos;
    private Image image = new Image("resources/views/res/redcoin.png");
    private static final int MAX_PLACEMENT_ATTEMPTS = 200;

    public Coin(int x, int y, Snake snake, Obstacle obstacle) {
        Random rnd = new Random();
        Point candidate = new Point();
        for (int attempt = 0; attempt < MAX_PLACEMENT_ATTEMPTS; attempt++) {
            Point p = new Point(rnd.nextInt(x), rnd.nextInt(y));
            if (!snake.getHead().equals(p) && !snake.getBody().contains(p)
                    && !obstacle.getObstacles().contains(p)) {
                candidate = p;
                break;
            }
        }
        pos = candidate;
    }

    public Point getPos() {
        return new Point(pos);
    }

    public int getPoints() {
        int points = 5;
        return points;
    }

    public Image getImage() {
        return image;
    }
}
