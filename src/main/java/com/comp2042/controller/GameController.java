package com.comp2042.controller;

import com.comp2042.Main;
import com.comp2042.model.*;
import com.comp2042.view.GameView;
import com.comp2042.view.ViewManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class GameController {
    private Snake snake;
    private Coin coin;
    private GameView gameView;
    private Game game;
    private final Stage stage;
    private Obstacle obstacle;
    private final CoinFactory coinFactory;

    private KeyCode key = KeyCode.LEFT;
    private KeyCode lastKey = KeyCode.LEFT;
    private boolean keyPressed = false;

    public GameController(Snake snake, Coin coin, Game game, GameView gameView, Obstacle obstacle, Stage stage) {
        this.snake = snake;
        this.coin = coin;
        this.gameView = gameView;
        this.game = game;
        this.stage = stage;

        this.obstacle = obstacle;
        this.coinFactory = new CoinFactory(game);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(game.getSpeed());
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        if (snake.getDead()) {
            return;
        } else {

            if (key.equals(KeyCode.LEFT)) {
                snake.move('L');
            } else if (key.equals(KeyCode.RIGHT)) {
                snake.move('R');
            } else if (key.equals(KeyCode.UP)) {
                snake.move('U');
            } else if (key.equals(KeyCode.DOWN)) {
                snake.move('D');
            }

            if (!snake.getDead() && snake.ateCoin(coin)) {
                applyCoinEffect();
                game.incScore();
                if (game.gameWon()) {
                    gameView.drawGrid(coin, snake, obstacle);
                    return;
                }
                spawnCoin();
            }

            keyPressed = false;

            if (!snake.getDead() || snake.hitSelf()) {
                gameView.drawGrid(coin, snake, obstacle);
            }

            gameView.gameWon();
            gameView.gameOver(snake);
            gameView.updateScoreLabel();
        }
    }));

    public void handle(KeyEvent event) {
        if (event.getCode().isArrowKey()) animation.play();

        if (isValidTurn(event.getCode()) && !keyPressed) {
            lastKey = key;
            key = event.getCode();
            keyPressed = true;
            gameView.turnDir(snake, lastKey, key);
        }

        if (event.getCode().equals(KeyCode.P)) {
            animation.stop();
        }

        if (event.getCode().equals(KeyCode.TAB)) {
            reset();
        }

        if ("+".equals(event.getText()) || event.getCode() == KeyCode.PLUS) {
            gameView.scaleToFullscreen(true, stage, obstacle, snake, coin);
            gameView.useLargeFonts();
        }

        if ("-".equals(event.getText()) || event.getCode() == KeyCode.MINUS) {
            gameView.scaleToFullscreen(false, stage, obstacle, snake, coin);
            gameView.useSmallFonts();
        }

        if (event.getCode().equals(KeyCode.ESCAPE)) {
            animation.stop();
            ViewManager viewManager = Main.getViewManager();
            viewManager.getGameStage().close();
            viewManager.getPrimaryStage().show();
        }
    }

    private boolean isValidTurn(KeyCode key) {
        if (!key.isArrowKey()) return false;
        else {
            if (key.equals(KeyCode.LEFT)) if (!this.key.equals(KeyCode.RIGHT)) return true;
            if (key.equals(KeyCode.RIGHT)) if (!this.key.equals(KeyCode.LEFT)) return true;
            if (key.equals(KeyCode.DOWN)) if (!this.key.equals(KeyCode.UP)) return true;
            if (key.equals(KeyCode.UP)) return !this.key.equals(KeyCode.DOWN);
            return false;
        }
    }

    private void reset() {

        animation.jumpTo(Duration.ZERO);
        animation.stop();

        snake = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
        coin = new Coin(game.getWidth(), game.getHeight(), snake, obstacle);

        animation.setRate(game.getSpeed());
        key = KeyCode.LEFT;
        keyPressed = false;
        game.resetScore();
        obstacle.reset();

        gameView.gameWon();
        gameView.gameOver(snake);
        gameView.updateScoreLabel();
        gameView.clearTurnMarkers();

        gameView.drawGrid(coin, snake, obstacle);
    }

    private void applyCoinEffect() {
        animation.setRate(game.getSpeed());
        snake.ignoreBorders(false);
        if (coin.getClass().equals(RainbowCoin.class)) {
            RainbowCoin tempCoin = (RainbowCoin) coin;
            animation.setRate(game.getSpeed() + tempCoin.getSpeedBonus());
        } else if (coin.getClass().equals(SilverCoin.class)){
            Random rnd = new Random();
            int before = obstacle.getObstacles().size();
            obstacle.newObstacles(snake, coin, rnd.nextInt(game.getWidth() * game.getHeight()/ 20) + 3);
            int added = obstacle.getObstacles().size() - before;
            game.decMaxScore(added);
        } else if (coin.getClass().equals(BlueCoin.class)) {
            snake.ignoreBorders(true);
        }
        game.addPoints(coin.getPoints());
    }

    private void spawnCoin() {
        coin = coinFactory.create(snake, obstacle);
    }

}
