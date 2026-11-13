package com.comp2042.model;

public class GoldCoin extends Coin {
    public GoldCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    @Override
    protected String getImageFile() {
        return "goldcoin.png";
    }

    @Override
    public int getPoints() {
        int points = 15;
        return points;
    }
}
