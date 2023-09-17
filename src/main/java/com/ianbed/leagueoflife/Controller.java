package com.ianbed.leagueoflife;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.image.BufferedImage;

public class Controller {
    final static int size = 1000;
    final static int window_width = 1000, window_height = 1000;
    final static int frameInterval = 5, generationalInterval = 100;
    int automata = 0, spawnQuantity = 15000;
    static boolean moveGenerations = true, mouseEvent = false;

    static int x_size = 5000, y_size = 5000;
    static int max_size = 200;
    static int interval = 0;
    static double scroll_speed = 10;

    static double x_accel, y_accel;
    static int scrolling = 0;
    static double m_x;
    static double m_y;
    int x_vel = 0, y_vel = 0;

    Board pixels;

    public ImageView imageBoard;
    @FXML

    public Image renderBoard(Board board) {
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

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public int colorize(int r, int g, int b)
    {
        return (r << 16) | (g << 8) | b;
    }

    public void updateVel()
    {
        x_vel += (x_accel / 10);
        y_vel += (y_accel / 10);

        if (x_accel == 0 && x_vel != 0)
            x_vel += (x_vel < 0) ? 1 : -1;

        if (y_accel == 0 && y_vel != 0)
            y_vel += (y_vel < 0) ? 1 : -1;
    }

    public void threadedGenerator(Board board, boolean updater)
    {
        Thread thread = new Thread("Generational Renderer") {
            public void run() {
                // 100 - interval spacing between zoom and update.
                if (updater && interval % generationalInterval == 0)
                {
                    board.progressGeneration(automata);
                    Image updated = renderBoard(board);
                    imageBoard.setImage(updated);
                }
            }
        };
        thread.start();
    }

    public void updateBoard(Board board, boolean updater) {
        double x, y;
        double top, left;
        Rectangle2D nuevo;

        threadedGenerator(board, updater);

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

        new_x = left + (x_vel/2.0) * (x/x_size);
        new_y = top + (y_vel/2.0) * (y/y_size);

        if (new_x < 0)
        {
            new_x = 1;
            x_accel = 0;
        }
        /// MAGIC NUMBERS _ screen width and lenght ,,.
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

        if (scrolling == 1 && x > 300)
        {
            new_x += scroll_speed;
            new_y += scroll_speed;
            x -= 2 * scroll_speed;
            y -= 2 * scroll_speed;
        }
        else if (scrolling == -1 && x/(x_size/size) < window_width)
        {
            new_x -= scroll_speed;
            new_y -= scroll_speed;
            x += 2 * scroll_speed;
            y += 2 * scroll_speed;
        }

        if (mouseEvent)
        {
            int click_x = (int) ((new_x + (x * (m_x/window_width))) / 5);
            int click_y = (int) ((new_y + (y * ((m_y + 110)/window_height))) / 5);
            tileAtPoint(click_x, click_y);
        }

        updateVel();

        nuevo = new Rectangle2D(new_x, new_y,  x,  y);
        imageBoard.setViewport(nuevo);
        interval++;
    }

    public static void readKeyPress(KeyEvent press)
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

    public void tileAtPoint(int x, int y)
    {
        pixels.retrieve(x, y).setActive(true);
        imageBoard.setImage(renderBoard(pixels));
        System.out.println(y);
        mouseEvent = false;
    }

    public static void readMousePress(MouseEvent input)
    {
        if (input.isPrimaryButtonDown())
        {
            m_x = input.getX();
            m_y = input.getY();
            mouseEvent = true;
        }
    }

    @FXML
    public void initialize() {
        pixels = new Board(size);
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