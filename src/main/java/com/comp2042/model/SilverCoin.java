package com.comp2042.model;

public class SilverCoin extends Coin {
    public SilverCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    @Override
    protected String getImageFile() {
        return "silvercoin.png";
    }

    @Override
    public int getPoints() {
        int points = 5;
        return points;
    }
}
