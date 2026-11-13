package com.comp2042.model;

public class BlueCoin extends Coin {
    public BlueCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    @Override
    protected String getImageFile() {
        return "bluecoin.png";
    }

    @Override
    public int getPoints() {
        int points = 5;
        return points;
    }
}
