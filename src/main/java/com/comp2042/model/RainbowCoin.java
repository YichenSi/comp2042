package com.comp2042.model;

public class RainbowCoin extends Coin {
    public RainbowCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    @Override
    protected String getImageFile() {
        return "rainbowcoin.png";
    }

    @Override
    public int getPoints() {
        int points = 5;
        return points;
    }

    public double getSpeedBonus() {
        double speedBonus = 5;
        return speedBonus;
    }
}
