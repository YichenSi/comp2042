package com.comp2042.model;

public class LevelManager {

    public static final int POINTS_PER_LEVEL = 50;

    private int level = 1;

    public int getLevel() {
        return level;
    }

    public  boolean update(int scorePoints) {
        int newLevel = 1 + scorePoints / POINTS_PER_LEVEL;
        if (newLevel > level) {
            level = newLevel;
            return true;
        }
        return false;
    }


    public double getSpeedBonus() {
        return (level - 1) * 1.5;
    }

    public int getExtraObstacles() {
        return (level - 1) * 2;
    }

    public void reset() {
        level = 1;
    }
}
