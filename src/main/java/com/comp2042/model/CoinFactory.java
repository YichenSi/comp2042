package com.comp2042.model;

import java.util.Random;

public class CoinFactory {

    private static final int ROLL_RANGE = 12;

    private final Game game;
    private final Random random;

    public CoinFactory(Game game) {
        this(game, new Random());
    }

    CoinFactory(Game game, Random random) {
        this.game = game;
        this.random = random;
    }

    public Coin create(Snake snake, Obstacle obstacle) {
        if (!game.isPowerUp()) {
            return new Coin(game.getWidth(), game.getHeight(), snake, obstacle);
        }

        int roll = random.nextInt(ROLL_RANGE);
        if (roll == 1) {
            return new GoldCoin(game.getWidth(), game.getHeight(), snake, obstacle);
        }
        if (roll == 2) {
            return new RainbowCoin(game.getWidth(), game.getHeight(), snake, obstacle);
        }
        if (roll == 3) {
            return new BlueCoin(game.getWidth(), game.getHeight(), snake, obstacle);
        }
        if (roll == 4 && obstacle.getObstacles().isEmpty()) {
            return new SilverCoin(game.getWidth(), game.getHeight(), snake, obstacle);
        }
        return new Coin(game.getWidth(), game.getHeight(), snake, obstacle);
    }
}
