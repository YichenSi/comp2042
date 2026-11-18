package com.comp2042.controller;

import com.comp2042.Main;
import com.comp2042.model.Coin;
import com.comp2042.model.Game;
import com.comp2042.model.Obstacle;
import com.comp2042.model.Snake;
import com.comp2042.view.GameView;
import com.comp2042.view.ViewManager;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private static final boolean POWER_UP = true;
    private static final char DIFFICULTY = 'N';

    private Scene gameScene;

    private ViewManager viewManager = Main.getViewManager();

    public void handleStart() {
        viewManager.initGameStage();

        Stage gameStage = viewManager.getGameStage();
        launchSnake(gameStage);
        gameStage.setScene(gameScene);
        viewManager.getPrimaryStage().close();
        gameStage.show();
    }

    private void launchSnake(Stage gameStage) {
        Game game = new Game(BOARD_WIDTH, BOARD_HEIGHT, POWER_UP, DIFFICULTY);
        Obstacle obstacle = new Obstacle(game.getWidth(), game.getHeight());
        Snake snake = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
        Coin coin = new Coin(game.getWidth(), game.getHeight(), snake, obstacle);

        GameView gameView = new GameView(game);
        GameController controller = new GameController(snake, coin, game, gameView, obstacle, gameStage);

        BorderPane root = new BorderPane();
        StackPane boardPane = new StackPane();
        gameScene = new Scene(root, game.getWidth() * game.getMultiplier(), game.getHeight() * game.getMultiplier() + GameView.HUD_HEIGHT);

        gameScene.setOnKeyPressed(controller::handle);

        gameView.makeScene(gameScene, snake, coin, obstacle);
        gameView.attachTo(root, boardPane);
        controller.showBestScore();
    }

    public ViewManager getViewManager() {
        return viewManager;
    }
}
