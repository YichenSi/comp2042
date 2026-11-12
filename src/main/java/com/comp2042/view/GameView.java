package com.comp2042.view;

import com.comp2042.model.Coin;
import com.comp2042.model.Game;
import com.comp2042.model.Obstacle;
import com.comp2042.model.Snake;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import java.awt.*;

public class GameView {

    private Canvas gridCanvas;
    private GraphicsContext gc;

    private Label gameOverLabel;
    private Label gameWonLabel;
    private Label scoreLabel;
    private boolean gameOverVis = false;
    private boolean gameWonVis = false;
    private final int[][] turnArray;

    private Game game;
    private double initialWidth;
    private double initialHeight;
    private double fullScale = 1;
    private boolean isFullscreen = false;

    public GameView(Game game) {
        this.game = game;
        this.turnArray = new int[game.getWidth()][game.getHeight()];
    }

    public void makeScene(Scene scene, Snake snake, Coin coin, Obstacle obstacle) {
        Scene gameScene = scene;
        gridCanvas = new Canvas(gameScene.getWidth(), gameScene.getHeight());
        initialWidth = scene.getWidth();
        initialHeight = scene.getHeight();
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(coin, snake, obstacle);


        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setId("gameOverLabel");
        String css = "resources/views/styling.css";
        gameOverLabel.getStylesheets().add(css);
        gameOverLabel.setVisible(false);

        gameWonLabel = new Label("YOU WON!");
        gameWonLabel.setId("gameWonLabel");
        gameWonLabel.getStylesheets().add(css);
        gameWonLabel.setVisible(false);

        scoreLabel = new Label("Score: " + game.getScorePoints());
        scoreLabel.setId("scoreLabel");
        scoreLabel.getStylesheets().add(css);
        scoreLabel.setFont(new Font("Arial", 20));
        scoreLabel.setVisible(true);
    }

    public void scaleToFullscreen(boolean fullScreen, Stage primaryStage, Obstacle obstacle, Snake snake, Coin coin) {
        primaryStage.setFullScreen(fullScreen);

        double xScaling = Screen.getPrimary().getVisualBounds().getHeight() / initialHeight;
        double yScaling = Screen.getPrimary().getVisualBounds().getWidth() / initialWidth;

        if (fullScreen && !isFullscreen) {
            if (xScaling < yScaling) {
                fullScale = xScaling;
            } else {
                fullScale = yScaling;
            }
            gridCanvas.setWidth(gridCanvas.getWidth() * fullScale);
            gridCanvas.setHeight(gridCanvas.getHeight() * fullScale);
            isFullscreen = true;
        } else if (!fullScreen) {
            fullScale = 1;
            gridCanvas.setWidth(initialWidth);
            gridCanvas.setHeight(initialHeight);
            isFullscreen = false;
        }

        gameOverLabel.setFont(new Font("Arial", 25 * fullScale));
        gameWonLabel.setFont(new Font("Arial", 25 * fullScale));
        drawGrid(coin, snake, obstacle);
    }

    public void drawGrid(Coin coin, Snake snake, Obstacle obstacle) {
        gc.setFill(game.getColor());
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.drawImage(coin.getImage(), (coin.getPos().x * game.getMultiplier()) * fullScale, (coin.getPos().y * game.getMultiplier()) * fullScale, game.getMultiplier() * fullScale - 3 * fullScale, game.getMultiplier() * fullScale - 3 * fullScale);

        int bodyIndex = 0;
        for (Point point : snake.getBody()) {
            drawBody(snake, point, bodyIndex);
            bodyIndex++;
        }

        drawHead(snake);

        gc.setFill(obstacle.getColor());
        for (Point point : obstacle.getObstacles()) {
            gc.fillRect(point.x * game.getMultiplier() * fullScale, point.y * game.getMultiplier() * fullScale, game.getMultiplier() * fullScale, game.getMultiplier() * fullScale);
        }
    }

    private Image rotate(ImageView view, double degrees) {
        view.setRotate(degrees);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return view.snapshot(params, null);
    }

    private void drawHead(Snake snake) {
        Image rotatedImage = rotate(snake.getImageViewHead(), snake.getSnakeDir());
        double size = game.getMultiplier() * fullScale;
        gc.drawImage(rotatedImage, snake.getHead().x * size, snake.getHead().y * size, size, size);
    }

    private void drawBody(Snake snake, Point point, int index) {
        Image rotatedImage;
        if (snake.getBody().size() <= 1) {
            rotatedImage = rotate(snake.getImageViewTail(), snake.getSnakeDir());
        } else if (index == 0) {
            int rotateTail;
            if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() && snake.getBody().get(1).getY() == snake.getBody().get(0).getY() - 1) {
                rotateTail = 0;
            } else if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() && snake.getBody().get(1).getY() == snake.getBody().get(0).getY() + 1) {
                rotateTail = 180;
            } else if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() - 1 && snake.getBody().get(1).getY() == snake.getBody().get(0).getY()) {
                rotateTail = 270;
            } else {
                rotateTail = 90;
            }
            rotatedImage = rotate(snake.getImageViewTail(), rotateTail);
        } else if (index > 0 && (turnArray[(int) snake.getBody().get(index).getX()][(int) snake.getBody().get(index).getY()] != 0)) {
            rotatedImage = rotate(snake.getImageViewTurn(), 90 * turnArray[(int) snake.getBody().get(index).getX()][(int) snake.getBody().get(index).getY()]);
        } else {
            rotatedImage = rotate(snake.getImageViewBody(), snake.getBodyDir(index));
        }

        if (turnArray[(int) snake.getBody().get(0).getX()][(int) snake.getBody().get(0).getY()] != 0) {
            turnArray[(int) snake.getBody().get(0).getX()][(int) snake.getBody().get(0).getY()] = 0;
        }
        double size = game.getMultiplier() * fullScale;
        gc.drawImage(rotatedImage, point.x * size, point.y * size, size, size);
    }

    public void turnDir(Snake snake, KeyCode lastKey, KeyCode key) {
        if ((lastKey == KeyCode.LEFT && key == KeyCode.UP) || (lastKey == KeyCode.DOWN && key == KeyCode.RIGHT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 2;
        } else if ((lastKey == KeyCode.LEFT && key == KeyCode.DOWN) || (lastKey == KeyCode.UP && key == KeyCode.RIGHT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 3;
        } else if ((lastKey == KeyCode.RIGHT && key == KeyCode.UP) || (lastKey == KeyCode.DOWN && key == KeyCode.LEFT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 1;
        } else if ((lastKey == KeyCode.UP && key == KeyCode.LEFT) || (lastKey == KeyCode.RIGHT && key == KeyCode.DOWN)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 4;
        } else turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 0;
    }

    public void clearTurnMarkers() {
        for (int x = 0; x < turnArray.length; x++) {
            for (int y = 0; y < turnArray[x].length; y++) {
                turnArray[x][y] = 0;
            }
        }
    }

    public void gameOver(Snake snake) {
        if (snake.getDead() && !gameOverVis) {
            gameOverVis = true;
            gameOverLabel.setVisible(true);
        } else if (!snake.getDead() && gameOverVis) {
            gameOverLabel.setVisible(false);
            gameOverVis = false;
        }
    }

    public void gameWon() {
        if (game.gameWon() && !gameWonVis) {
            gameWonVis = true;
            gameWonLabel.setVisible(true);
        } else if (!game.gameWon() && gameWonVis) {
            gameWonVis = false;
            gameWonLabel.setVisible(false);
        }
    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + game.getScorePoints());
    }

    public void attachTo(StackPane pane) {
        pane.getChildren().addAll(gridCanvas, gameOverLabel, gameWonLabel, scoreLabel);
        StackPane.setAlignment(gameOverLabel, Pos.TOP_CENTER);
        StackPane.setAlignment(gameWonLabel, Pos.TOP_CENTER);
        StackPane.setAlignment(scoreLabel, Pos.BOTTOM_CENTER);
    }

    public void useLargeFonts() {
        gameOverLabel.setStyle("-fx-font-size: 50;");
        gameWonLabel.setStyle("-fx-font-size: 50;");
        scoreLabel.setStyle("-fx-font-size: 35;");
    }

    public void useSmallFonts() {
        gameOverLabel.setStyle("-fx-font-size: 15;");
        gameWonLabel.setStyle("-fx-font-size: 15;");
        scoreLabel.setStyle("-fx-font-size: 15;");
    }

}
