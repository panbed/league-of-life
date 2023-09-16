package com.ianbed.leagueoflife;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Random;

public class Controller {
    final static int size = 1000;
    final static int frameInterval = 10;
    static int interval = 0;
    public ImageView imageBoard;
    @FXML
    private Label welcomeText;
    @FXML
    private Canvas grid;

//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }

    public Image renderBoard(Board board) {
        // create our image to render onto
        BufferedImage bufferedImage = new BufferedImage(5000, 5000, BufferedImage.TYPE_INT_RGB);
        int r = 255, g = 0, b = 0; // color: red
        int col = colorize(r, g, b); // bitshift to create int for color
        // iterate through all pixels, if it should be active, then set the color on the image to active as well
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).isActive()) {
                    // Scale so one tile is spread over a 5x5 area
                    for (int i = 0; i < 5; i++)
                        for (int j = 0; j < 5; j++)
                            bufferedImage.setRGB((x*5+i)%5000, (y*5+j)%5000, col);

                }
            }
        }

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public int colorize(int r, int g, int b)
    {
        return (r << 16) | (g << 8) | b;
    }

    public void updateBoard(Board board, boolean updater) {
        double x, y;

        if (imageBoard.getViewport() != null)
        {
            x = imageBoard.getViewport().getHeight();
            y = imageBoard.getViewport().getHeight();
        }
        else
        {
            x = imageBoard.getFitWidth();
            y = imageBoard.getFitHeight();
        }

        if (x < 200 || y < 200)
        {
            x = 200;
            y = 200;
        }
        // this is where the magic (zoomeing) se ocurio
        Rectangle2D nuevo = new Rectangle2D(0, 0,  x -1,  y-1 );
        imageBoard.setSmooth(true);
        imageBoard.setViewport(nuevo);
        Thread thread = new Thread("Generational Renderer") {
            public void run() {
                if (updater && interval % 100 == 0)
                {
                    board.progressGeneration(0);
                    Image updated = renderBoard(board);
                    imageBoard.setImage(updated);
                }
            }
        };
        thread.start();


        interval++;
    }

    @FXML
    public void initialize() {
        Board pixels = new Board(size);
        pixels.randomPixelPlacement(50000);
        boolean active = false;

        // use timeline to update board without stopping the main gui thread
        Timeline boardUpdater = new Timeline();
        KeyFrame cool = new KeyFrame(Duration.millis(frameInterval), event -> updateBoard(pixels, true));
        boardUpdater.getKeyFrames().add(cool);

        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();
    }
}