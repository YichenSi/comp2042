package com.comp2042;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Obstacle {
    private Color color = Color.BLACK;
    private ArrayList<Point> obstacles = new ArrayList<>();
    private int x;
    private int y;
    private static final int MAX_PLACEMENT_ATTEMPTS = 200;
    private static final int MAX_OBSTACLES = 50;

    public Obstacle(int x, int y) {
       this.x = x;
       this.y = y;
    }

    public void newObstacles(Snake snek, Coin coin, int amount) {
        Random rnd = new Random();
        for (int i=0; i<amount && obstacles.size() < MAX_OBSTACLES; i++) {
            Point candidate = null;
            for (int attempt = 0; attempt < MAX_PLACEMENT_ATTEMPTS; attempt++) {
                Point p = new Point(rnd.nextInt(x), rnd.nextInt(y));
                if (!isOccupied(p, snek, coin)) {
                    candidate = p;
                    break;
                }
            }
            if (candidate != null) obstacles.add(candidate);
        }
    }

    private boolean isOccupied(Point p, Snake snek, Coin coin) {
        return obstacles.contains(p)
                || coin.getPos().equals(p)
                || snek.getBody().contains(p)
                || snek.getHead().equals(p);
    }


    public Color getColor() {
        return color;
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public void reset() {
        obstacles.clear();
    }
}
