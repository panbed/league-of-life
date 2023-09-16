package com.ianbed.leagueoflife;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Random;

public class Controller {
    final static int size = 1000;
    final static int frameInterval = 125;
    public ImageView imageBoard;
    @FXML
    private Label welcomeText;
    @FXML
    private Canvas grid;

//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }

    public void renderBoard(Board board) {
        // create our image to render onto
        BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        int r = 255, g = 0, b = 0; // color: red
        int col = colorize(r, g, b); // bitshift to create int for color
        // iterate through all pixels, if it should be active, then set the color on the image to active as well
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).isActive()) {
                    bufferedImage.setRGB(x, y, col);
                }
            }
        }

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        // try zooming!!
        imageBoard.setImage(image); // change the image displayed in javafx to the newly created image
    }

    public int colorize(int r, int g, int b)
    {
        return (r << 16) | (g << 8) | b;
    }

    public void renderGrid(Board board) {
        GraphicsContext gc = grid.getGraphicsContext2D();
        gc.clearRect(0, 0, grid.getWidth(), grid.getHeight());
        PixelWriter pw = gc.getPixelWriter();
        pw.setColor(0,0, Color.RED);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).isActive()) {
                    pw.setColor(x,y, Color.RED);
                }
                else {
                    pw.setColor(x, y, Color.WHITE);
                }
            }
        }
    }

    public void updateBoard(Board board) {
        board.progressGeneration(0);
        renderBoard(board);
    }

    @FXML
    public void initialize() {
        Board pixels = new Board(size);
        pixels.randomPixelPlacement(5000);

        // use timeline to update board without stopping the main gui thread
        Timeline boardUpdater = new Timeline(
            new KeyFrame(Duration.millis(frameInterval), event -> updateBoard(pixels))
        );

        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();
    }
}