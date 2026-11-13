package com.comp2042.model;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Random;

public class Coin {
    private Point pos;
    private Image image;
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

    protected String getImageFile() {
        return "redcoin.png";
    }

    public Point getPos() {
        return new Point(pos);
    }

    public int getPoints() {
        int points = 5;
        return points;
    }

    public Image getImage() {
        if (image == null) {
            image = new Image(Coin.class.getResourceAsStream("/resources/views/res/" + getImageFile()));
        }
        return image;
    }
}
