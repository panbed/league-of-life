package com.ianbed.leagueoflife;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Random;

public class Controller {
    final static int size = 1000;
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
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        int r, g, b;
//        int col = (r)

    }

    public void renderGrid(Board board) {
        GraphicsContext gc = grid.getGraphicsContext2D();
        gc.clearRect(0, 0, grid.getWidth(), grid.getHeight());
        PixelWriter pw = gc.getPixelWriter();
        pw.setColor(0,0, Color.RED);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).getActive()) {
                    pw.setColor(x,y, Color.RED);
                }
                else {
                    pw.setColor(x, y, Color.WHITE);
                }
            }
        }
    }

    public void updateBoard(Board board) {
        board.progressGeneration();
        renderGrid(board);
//        board.printBoard();
    }

    @FXML
    public void initialize() throws Exception {
        Board pixels = new Board(size);

        pixels.retrieve(89, 55).setActive(true);
        pixels.retrieve(89, 56).setActive(true);
        pixels.retrieve(89, 54).setActive(true);
        pixels.retrieve(81, 55).setActive(true);
        pixels.retrieve(88, 56).setActive(true);
        pixels.retrieve(88, 54).setActive(true);

        pixels.retrieve(5,5).setActive(true);
        pixels.retrieve(5,4).setActive(true);
        pixels.retrieve(5,3).setActive(true);

        pixels.retrieve(4,5).setActive(true);
        pixels.retrieve(3,4).setActive(true);


//        updateBoard(pixels);
//        renderGrid(pixels);
//        pixels.printBoard();
//        pixels[5][4].setActive(true);
//        pixels[5][3].setActive(true);
//
//        pixels[4][5].setActive(true);
//        pixels[3][4].setActive(true);

//        new Thread(() -> Platform.runLater(() -> {
//            while(true) {
//
//                updateBoard(pixels);
//                try {
//                    Thread.sleep(250);
//                    renderGrid(pixels);
//                    Thread.sleep(250);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                renderGrid(pixels);
//                try {
//                    Thread.sleep(250);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                pixels.printBoard();
//
//            }
//        }));

        Random random = new Random();
        int cool, cool2;
        for (int i = 20; i < 500; i++) {
            cool = 20 + random.nextInt(950);
            cool2 = 20 + random.nextInt(950);
            pixels.retrieve(cool, cool2).setActive(true);
            pixels.retrieve(cool + 1, cool2 + 1).setActive(true);
            pixels.retrieve(cool - 1, cool2 + 1).setActive(true);
            pixels.retrieve(cool + 1, cool2 - 1).setActive(true);
            pixels.retrieve(cool - 1, cool2 - 1).setActive(true);
        }

        // use timeline to updateboard without stopping the main gui thread
        Timeline boardUpdater = new Timeline(
                new KeyFrame(Duration.millis(125), event -> {
//                        updateBoard(pixels);
                    Platform.runLater(() -> {
                        updateBoard(pixels);
                    });
                })
        );

        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();
    }
}