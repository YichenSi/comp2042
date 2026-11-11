package com.comp2042.model;

import javafx.scene.image.Image;

public class RainbowCoin extends Coin {
    private Image image = new Image("resources/views/res/rainbowcoin.png");

    public RainbowCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    public int getPoints() {
        int points = 5;
        return points;
    }

    public double getSpeedBonus() {
        double speedBonus = 5;
        return speedBonus;
    }

    public Image getImage() {
        return image;
    }
}
