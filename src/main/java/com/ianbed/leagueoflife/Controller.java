package com.ianbed.leagueoflife;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import java.io.File;
import java.security.Key;
import java.util.Random;

public class Controller {
    final static int size = 1000;
    public ImageView imageBoard;
    @FXML
    private GridPane gridPane;

//    public void renderBoard(Board board) {
//        // create our image to render onto
//        BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
//        int r = 255, g = 0, b = 0; // color: red
//        int col = (r << 16) | (g << 8) | b; // bitshift to create int for color
//        // iterate through all pixels, if it should be active, then set the color on the image to active as well
//        for (int x = 0; x < size; x++) {
//            for (int y = 0; y < size; y++) {
//                if (board.retrieve(x, y).isActive()) {
//                    bufferedImage.setRGB(x, y, col);
//                }
//            }
//        }
//
//        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//        imageBoard.setImage(image); // change the image displayed in javafx to the newly created image
//    }

    public void renderView(Board board, int ccx, int ccy, int zoom) {
        // ccx: center coordinate x, ccy: center coordinate y
        // create our image to render onto
        BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        int r = 0, g = 255, b = 0; // color: red
//        int col;
        int col = (r << 16) | (g << 8) | b; // bitshift to create int for color
//         iterate through all pixels, if it should be active, then set the color on the image to active as well
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).isActive()) {
                    bufferedImage.setRGB(x, y, col);
                }
            }
        }

//        // let's say by default you can only look at 100x100 pixels, centered
//        // calculate visible grids based on center coordinate
//        // e.g. (500, 500):
//        // display (500, 500) in middle, (400, 400) in top left, (600, 600) in bttm right
//        // (600, 400) top right, (400, 600) in bttm left
//        r = 0;
//        g = 0;
//        b = 0;
//        // drawing directly on BufferedImage
//        int totalColumnBlocks = size/10;
//        int blockLength = 10;
//        int xStart = ccx - 100;
//        int yStart = ccy - 100;
//        int xEnd = size - ccx;
//        int yEnd = size - ccy;
//        // if its 1000, each
//        for (int x = xStart; x < xEnd; x++) {
//            for (int y = yStart; y < yEnd; y++) {
//                if (board.retrieve(x, y).isActive()) {
//                    r = 255;
//                }
//                else {
//                    r = 0;
//                }
//                col = (r << 16) | (g << 8) | b;
//                // create square
//                for (int i = (xStart + x) - xStart; i < blockLength; i++) {
//                    for (int j = (yStart + y) - yStart; j < blockLength; j++) {
//                        bufferedImage.setRGB(i, j, col);
//                    }
//                }
//            }
//        }

        // convert buffered image to image with tofximage TODO: apparentely you can make this faster
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageBoard.setSmooth(false);
        imageBoard.setViewport(new Rectangle2D(500, 500, 100, 100));
        imageBoard.setSmooth(false);
        imageBoard.setImage(image); // change the image displayed in javafx to the newly created image
    }

//    public void

    public void scale(double scale) {
        int currentWidth = (int) imageBoard.getFitWidth();
        int currentHeight = (int) imageBoard.getFitHeight();
        int endWidth = (int) (imageBoard.getFitWidth() * scale);
        int endHeight = (int) (imageBoard.getFitHeight() * scale);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame scaleImageView = new KeyFrame(
                Duration.millis(500),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
//                        imageBoard.setViewport(new Rectangle2D(1000, 1000, ));
                    }
                }
        );
    }

    // DEPRECATED: use renderBoard
//    public void renderGrid(Board board) {
//        GraphicsContext gc = grid.getGraphicsContext2D();
//        gc.clearRect(0, 0, grid.getWidth(), grid.getHeight());
//        PixelWriter pw = gc.getPixelWriter();
//        pw.setColor(0,0, Color.RED);
//        for (int x = 0; x < size; x++) {
//            for (int y = 0; y < size; y++) {
//                if (board.retrieve(x, y).getActive()) {
//                    pw.setColor(x,y, Color.RED);
//                }
//                else {
//                    pw.setColor(x, y, Color.WHITE);
//                }
//            }
//        }
//    }

    public void updateBoard(Board board) {
        board.progressGeneration();
//        renderGrid(board);
        renderView(board, 500, 500, 1);
    }

    @FXML
    public void initialize() throws Exception {
//        imageBoard.fitWidthProperty().bind(gridPane.widthProperty());
        imageBoard.fitWidthProperty().bind(gridPane.heightProperty());
        imageBoard.setPreserveRatio(false);
        ;

        Board pixels = new Board(size); // create board
        // probably temporary: spawn a bunch of random pixels
        Random random = new Random();
        int cool, cool2;
        for (int i = 20; i < 70000; i++) {
            cool = 20 + random.nextInt(900);
            cool2 = 20 + random.nextInt(909);
            pixels.retrieve(cool, cool2).setActive(true);
            pixels.retrieve(cool + 1, cool2 + 1).setActive(true);
            pixels.retrieve(cool - 1, cool2 + 1).setActive(true);
            pixels.retrieve(cool + 1, cool2 - 1).setActive(true);
            pixels.retrieve(cool - 1, cool2 - 1).setActive(true);
        }

        imageBoard.setOnMouseClicked(event -> {
            int x, y;
            x = (int) event.getSceneX();
            y = (int) event.getSceneY();
            System.out.println(x + ", " + y);
            System.out.println("creating tile...");

        });

        // use timeline to update board without stopping the main gui thread
        Timeline boardUpdater = new Timeline(
                new KeyFrame(Duration.millis(125), event -> {
                    updateBoard(pixels);
                })
        );

        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();
    }
}