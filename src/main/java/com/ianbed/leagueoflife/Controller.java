package com.ianbed.leagueoflife;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Controller {
    final static int size = 1000;
    final static int window_width = 1000, window_height = 800;
    final static int frameInterval = 5, generationalInterval = 100;

    int automata = 0, spawnQuantity = 500;
    static boolean moveGenerations = true;

    static int x_size = 5000, y_size = 5000;
    static int max_size = 200;
    static int interval = 0;
    static double scroll_speed = 10;

    static int x_accel, y_accel, scrolling = 0;
    int x_vel = 0, y_vel = 0;

    int topLeftMinimapX, topLeftMinimapY, miniMapFocusWidth, miniMapFocusHeight;

    @FXML
    public ImageView imageBoard;
    private Label welcomeText;
    private Canvas grid;
    public GridPane gridPane;
    public Label manaLabel;
    public ImageView minimap;

    public ImageView hotbar1; // mazda
    public ImageView hotbar2; // diamoeba
    public ImageView hotbar3; //
    public ImageView hotbar4; // vote
    public ImageView hotbar5; //
    public ImageView hotbar6; //

    public BufferedImage renderBoard(Board board) {
        // create our image to render onto
        BufferedImage bufferedImage = new BufferedImage(x_size, y_size, BufferedImage.TYPE_INT_RGB);
        int r = 255, g = 255, b = 255;
        int col = colorize(r, g, b); // bitshift to create int for color
        // iterate through all pixels, if it should be active, then set the color on the image to active as well
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.retrieve(x, y).isActive()) {
                    // Scale so one tile is spread over a 5x5 area
                    for (int i = 0; i < 5; i++)
                        for (int j = 0; j < 5; j++)
                            bufferedImage.setRGB((x*5+i) % x_size, (y*5+j) % y_size, col);
                }
            }
        }

//        Image generatedImage = SwingFXUtils.toFXImage(bufferedImage, null);
        // return BufferedImage in case we need to add more pixels to it, e.g. in the minimap
        return bufferedImage;
    }

    // convert the BufferedImage to an image to use in ImageViews, etc
    public Image convertBufferedImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public void renderMinimap(BufferedImage bufferedImage) {
        int red = 150, green = 0, blue = 255;
        int col = colorize(red, green, blue);

        // creating the rectangle
        for (int i = topLeftMinimapX; i < topLeftMinimapX + miniMapFocusWidth; i++) {
            for (int a = 0; a < 30; a++) {
                bufferedImage.setRGB(i % x_size, (topLeftMinimapY + a) % y_size, col);
                bufferedImage.setRGB(i % x_size, ((topLeftMinimapY + miniMapFocusHeight) + a) % y_size, col);
            }
        }
        for (int j = topLeftMinimapY; j < topLeftMinimapY + miniMapFocusHeight; j++) {
            for (int c = 0; c < 30; c++) {
                bufferedImage.setRGB((topLeftMinimapX + c) % x_size, j % y_size, col);
                bufferedImage.setRGB(((topLeftMinimapX + miniMapFocusWidth) + c) % x_size, j % y_size, col);
            }
        }

//        for (int i = topLeftMinimapX; i < topLeftMinimapX + miniMapFocusWidth; i++) {
//            for (int a = 0; a < 5; a++)
//                for (int b = 0; b < 5; b++)
//                    bufferedImage.setRGB((i*5+a) % x_size, (i*5), col);
//        }
//        for (int j = topLeftMinimapY; j < topLeftMinimapY + miniMapFocusHeight; j++) {
//            for (int c = 0; c < 5; c++)
//                for (int d = 0; d < 5; d++)
//                    bufferedImage.setRGB((j*5), (j*5+d) % y_size, col);
//        }
//        for (int k = topLeftMinimapX; k < topLeftMinimapX + miniMapFocusWidth; k++) {
//            for (int e = 0; e < 5; e++)
//                for (int f = 0; f < 5; f++)
//                    bufferedImage.setRGB((k*5+e) % x_size, ((k*5) + miniMapFocusHeight) % y_size, col);
//        }
//        for (int l = topLeftMinimapY; l < topLeftMinimapY + miniMapFocusHeight; l++) {
//            for (int g = 0; g < 5; g++)
//                for (int h = 0; h < 5; h++)
//                    bufferedImage.setRGB((l*5) % x_size, (l*5+h) % y_size, col);
//        }

        minimap.setImage(convertBufferedImage(bufferedImage));
    }

    public void updateMinimapRectangleBounds(Rectangle2D rectangle2D) {
        topLeftMinimapX = (int) rectangle2D.getMinX();
        topLeftMinimapY = (int) rectangle2D.getMinY();
        miniMapFocusWidth = (int) rectangle2D.getWidth();
        miniMapFocusHeight = (int) rectangle2D.getHeight();

//        System.out.println(bottomRight);
    }

    public int colorize(int r, int g, int b)
    {
        return (r << 16) | (g << 8) | b;
    }

    public void updateVel()
    {
        x_vel += x_accel / 10;
        y_vel += y_accel / 10;

        if (x_accel == 0 && x_vel != 0)
        {
            x_vel += (x_vel < 0) ? 1 : -1;
        }

        if (y_accel == 0 && y_vel != 0)
        {
            y_vel += (y_vel < 0) ? 1 : -1;
        }
    }

    public void updateBoard(Board board, boolean updater) {
        double x, y;
        double top, left;

        updateVel();

        Thread thread = new Thread("Generational Renderer") {
            public void run() {
                // 100 - interval spacing between zoom and update.
                if (updater && interval % generationalInterval == 0)
                {
                    board.progressGeneration(automata);
                    BufferedImage bufferedImage = renderBoard(board);
                    Image updated = convertBufferedImage(bufferedImage);
                    imageBoard.setImage(updated);
                    renderMinimap(bufferedImage);
                }
            }
        };
        thread.start();

        Rectangle2D nuevo;

        if (imageBoard.getViewport() != null)
        {
            x = imageBoard.getViewport().getWidth();
            y = imageBoard.getViewport().getHeight();
            top = imageBoard.getViewport().getMinY();
            left = imageBoard.getViewport().getMinX();
        }
        else
        {
            x = x_size;
            y = y_size;
            top = 0;
            left = 0;
        }

        if (x < max_size || y < max_size)
        {
            x = max_size;
            y = max_size;
        }

        double new_x, new_y;

        new_x = left+(x_vel/2.0);
        new_y = top+(y_vel/2.0);

        if (new_x < 0)
        {
            new_x = 1;
            x_accel = 0;
        }
//        /// MAGIC NUMBERS _ screen width and lenght ,,.
        else if (new_x + x  > x_size)
        {
            new_x = x_size - x;
            x_accel = 0;
        }

        if (new_y < 0)
        {
            new_y = 1;
            y_accel = 0;
        }
        else if (new_y + y  > y_size)
        {
            new_y = y_size - y;
            y_accel = 0;
        }

//        // this is where the magic (zoomeing) se ocurio
        if (scrolling == 1)
        {
            new_x += scroll_speed;
            new_y += scroll_speed;
            x -= 2 * scroll_speed;
            y -= 2 * scroll_speed;
        }
        else if (scrolling == -1)
        {
            new_x -= scroll_speed;
            new_y -= scroll_speed;
            x += 2 * scroll_speed;
            y += 2 * scroll_speed;
        }

        nuevo = new Rectangle2D(new_x, new_y,  x,  y);
        updateMinimapRectangleBounds(nuevo);

        imageBoard.setViewport(nuevo);
        interval++;
    }

    public static void readKeypress(KeyEvent press)
    {
        if (press.getText().equals("a"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                x_accel = -10;
            else
                x_accel = 0;
        }
        else if (press.getText().equals("d"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                x_accel = 10;
            else
                x_accel = 0;
        }
        else if (press.getText().equals("w"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                y_accel = -10;
            else
                y_accel = 0;
        }
        else if (press.getText().equals("s"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                y_accel = 10;
            else
                y_accel = 0;
        }
        else if (press.getText().equals("q"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                scrolling = 1;
            else
                scrolling = 0;
        }
        else if (press.getText().equals("e"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                scrolling = -1;
            else
                scrolling = 0;
        }
        else if (press.getText().equals("x"))
        {
            if (press.getEventType().getName().equals("KEY_PRESSED"))
                moveGenerations = !moveGenerations;
        }
    }

    @FXML
    public void initialize() {
        Board pixels = new Board(size);
        pixels.randomPixelPlacement(spawnQuantity);
        boolean active = false;

        // use timeline to update board without stopping the main gui thread
        Timeline boardUpdater = new Timeline();
        KeyFrame cool = new KeyFrame(Duration.millis(frameInterval), event -> updateBoard(pixels, moveGenerations));
        boardUpdater.getKeyFrames().add(cool);

        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();
    }
}