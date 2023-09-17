package com.ianbed.leagueoflife;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javafx.scene.control.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Controller {
    final static int size = 1000;
    final static int window_width = 1000, window_height = 1000;
    final static int frameInterval = 5, generationalInterval = 100;
    public Rectangle coolSquareClip;
    public Label informationLabel;
    public Label currentAlgorithm;
    public HBox algorithmBox;
    public Label pauseText;

    int automata = 0, spawnQuantity = 15000;
    boolean gameStarted = false;
    static boolean moveGenerations = false, mouseEvent = false;

    static int x_size = 5000, y_size = 5000;
    static int max_size = 200;
    static int interval = 0;
    static double scroll_speed = 10;

    static double x_accel, y_accel;
    static int scrolling = 0;
    static double m_x;
    static double m_y;
    int x_vel = 0, y_vel = 0;
    int topLeftMinimapX, topLeftMinimapY, miniMapFocusWidth, miniMapFocusHeight;

    @FXML
    public ImageView imageBoard;
    private Label welcomeText;
//    private Canvas grid;
    public GridPane gridPane;
    public Label manaLabel;
    public ImageView minimap;

    public ImageView hotbar1; // mazda
    public ImageView hotbar2; // diamoeba
    public ImageView hotbar3; // highlife
    public ImageView hotbar4; // vote
    public ImageView hotbar5; // serviettes
    public ImageView hotbar6; // life

    public MediaView backgroundVideo;
    public Label mainMenuBody;
    public Rectangle playButtonRect;
    public Label playButtonLabel;
    public Pane menuPane;
    public AnchorPane gamePane;
    public Circle manaCircleRadius;

    Board pixels;
  
  
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
        int red = 254, green = 254, blue = 254;
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

        minimap.setImage(convertBufferedImage(bufferedImage));
    }

    public void updateMinimapRectangleBounds(Rectangle2D rectangle2D) {
        topLeftMinimapX = (int) rectangle2D.getMinX();
        topLeftMinimapY = (int) rectangle2D.getMinY();
        miniMapFocusWidth = (int) rectangle2D.getWidth();
        miniMapFocusHeight = (int) rectangle2D.getHeight();
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
                    BufferedImage bufferedImage = renderBoard(board);
                    Image updated = convertBufferedImage(bufferedImage);
                    imageBoard.setImage(updated);
                    renderMinimap(bufferedImage);
                }
            }
        };
        thread.start();
    }

    public void updateBoard(Player player, Board board, boolean updater) {
        double x, y;
        double top, left;
        Rectangle2D nuevo;

        threadedGenerator(board, updater);
        manaLabel.setText(String.format("%d", player.getMana()));
        manaCircleRadius.setRadius(player.getMana() >= 17 ? player.getMana() - 17 : 17 - (83 % player.getMana()));
        setAlgorithmLabel(automata);
        togglePause(moveGenerations);

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
            tileAtPoint(player, click_x, click_y);
        }

        updateVel();

        nuevo = new Rectangle2D(new_x, new_y,  x,  y);
        updateMinimapRectangleBounds(nuevo);
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
            if (press.getEventType().getName().equals("KEY_PRESSED")) {
                moveGenerations = !moveGenerations;

            }

        }
    }

    public void togglePause(boolean movementEnabled) {
        if (!movementEnabled) {
            pauseText.setVisible(true);
        }
        else {
            pauseText.setVisible(false);
        }
    }

    public void tileAtPoint(Player player, int x, int y)
    {
        if (player.attemptCast(1)) {
            pixels.retrieve(x, y).setActive(true);
            imageBoard.setImage(convertBufferedImage(renderBoard(pixels)));
            mouseEvent = false;
        }
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

    public void setOpacity(Player player, ArrayList<ImageView> imageViews) {
        for (ImageView i : imageViews) {
            if (((int) i.getUserData()) > player.getMana()) {
                i.setOpacity(0.25);
            }
            else {
                i.setOpacity(1);
            }
        }
    }

    public void invertPlayButton(int opt) {
        if (opt == 1) {
            playButtonRect.setFill(Color.BLACK);
//            playButtonLabel.setStyle("-fx-text-fill: white");
        }
        else {
            playButtonRect.setFill(Color.WHITE);
//            playButtonLabel.setStyle("-fx-text-fill: black; -fx-font-size: 69px");
        }
    }

    public void hideMenuShowGame() {
        menuPane.setVisible(false);
        menuPane.setDisable(true);
        gamePane.setVisible(true);
        gamePane.setDisable(false);
        moveGenerations = true;
    }

    public void castSpellOverTime(Player player, int mana, int currentAutomata) {
        Timeline spellCaster = new Timeline();
        KeyFrame castUpdate = new KeyFrame(Duration.millis(500), event -> {
            if (!player.attemptCast(mana)) {
                automata = currentAutomata; // ran out of mana
            }
        });
        spellCaster.getKeyFrames().add(castUpdate);
        spellCaster.setCycleCount(5);
        spellCaster.play();
    }

    public void setHeroActions(Player player) {
        gridPane.setOnMouseClicked(event -> {
        });

        ImageView[] views = new ImageView[6];
        views[0] = hotbar1;
        views[1] = hotbar2;
        views[2] = hotbar3;
        views[3] = hotbar4;
        views[4] = hotbar5;
        views[5] = hotbar6;


        algorithmBox.setOnMouseExited(event -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO));
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500),
                    new KeyValue(informationLabel.opacityProperty(), 0));
            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(actionEvent -> {
                informationLabel.setText("");
                informationLabel.setOpacity(1);
            });
            timeline.play();
        });


        // EW EWE WEW EW EWEWWWWWWWWWWWWWWWWWW I DONT LIKE THIS BUT ITS GONNA HAVE TO GO IN FOR NOW
        hotbar1.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Maze. Pixels will create maze-like structures. Difficult to kill off, but uses a large amount of mana over a 2 second period.");
        });

        hotbar2.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Diamoeba. Pixels will consolidate and clump together. Cheap.");
        });

        hotbar3.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Highlife.\n\"It seems to me that 'B36/S23' is really the game I should have found, since it's so rich in nice things.\"");
        });

        hotbar4.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Vote. Consolidates into clumps and stay relatively stable.");
        });

        hotbar5.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Serviettes. An explosion, but only briefly, before you run out of mana.");
        });

        hotbar6.setOnMouseEntered(event -> {
            informationLabel.setOpacity(1);
            informationLabel.setText("Life. The classic Game of Life by John Conway.");
        });
        informationLabel.setText("");
        for (int i = 0; i < 6; i++) {
//            views[i].setOnMouseExited(event -> {
//                Timeline timeline = new Timeline();
//                KeyFrame keyFrame = new KeyFrame(
//                        Duration.millis(250),
//                        new KeyValue(informationLabel.opacityProperty(), 0));
//                timeline.getKeyFrames().add(keyFrame);
//                timeline.setOnFinished(actionEvent -> {
//                    informationLabel.setText("");
//                    informationLabel.setOpacity(1);
//                });
//                timeline.play();
//            });

        }

        hotbar1.setOnMouseClicked(event -> {
            if (player.attemptCast(9)) {
                automata = 5; // maze
            }
        });
        hotbar2.setOnMouseClicked(event -> {
            automata = 2; // diamoeba
            if (player.attemptCast(9)) {
                automata = 2; // diamoeba
            }
        });
        hotbar3.setOnMouseClicked(event -> {
            if (player.attemptCast(9)) {
                automata = 3; // highlife
            }
        });
        hotbar4.setOnMouseClicked(event -> {
            if (player.attemptCast(9)) {
                automata = 4; // vote
            }
        });
        hotbar5.setOnMouseClicked(event -> {
//            if (player.attemptCast(9)) {
//                automata = 1; // serviettes
//            }
            if (automata != 1) {
                castSpellOverTime(player, 9, automata);
            }
        });
        hotbar6.setOnMouseClicked(event -> {
            if (player.attemptCast(9)) {
                automata = 0; // life
            }
        });
    }

    public void setAlgorithmLabel(int choice) {
        switch (choice) {
            case 1 -> currentAlgorithm.setText("maze");
            case 2 -> currentAlgorithm.setText("diamoeba");
            case 3 -> currentAlgorithm.setText("highlife");
            case 4 -> currentAlgorithm.setText("vote");
            case 5 -> currentAlgorithm.setText("serviettes");
            default -> currentAlgorithm.setText("life");
        }
    }

    @FXML
    public void initialize() {
        playButtonRect.setOnMouseEntered(event -> invertPlayButton(1));
        playButtonRect.setOnMouseExited(event -> invertPlayButton(2));
        playButtonLabel.setOnMouseEntered(event -> invertPlayButton(1));
        playButtonLabel.setOnMouseExited(event -> invertPlayButton(2));

        playButtonRect.setOnMouseClicked(event -> hideMenuShowGame());
        playButtonLabel.setOnMouseClicked(event -> hideMenuShowGame());

        Media media = new Media(new File("src/main/resources/video/gameplay.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        backgroundVideo.setMediaPlayer(mediaPlayer);

        mainMenuBody.setText("Q/E zoom\nW/A/S/D pan\nX stop\n-> frame advance");

        Player chilleingandRelaxeing = new Player();

        pixels = new Board(size);
        pixels.randomPixelPlacement(spawnQuantity);
        boolean active = false;

        ArrayList<ImageView> imageViews = new ArrayList<>();
        // cool,  get all the cool hotbar items so that i dont have to repeat
        // ( i think )
        imageViews.add(hotbar1);
        imageViews.add(hotbar2);
        imageViews.add(hotbar3);
        imageViews.add(hotbar4);
        imageViews.add(hotbar5);
        imageViews.add(hotbar6);

        Timeline hotbarUpdater = new Timeline();
        KeyFrame updateHotbar = new KeyFrame(Duration.millis(100), event -> {
            setOpacity(chilleingandRelaxeing, imageViews);
        });
        hotbarUpdater.getKeyFrames().add(updateHotbar);
        hotbarUpdater.setCycleCount(Timeline.INDEFINITE);
        hotbarUpdater.play();

        // use timeline to update board without stopping the main gui thread
        Timeline boardUpdater = new Timeline();
        KeyFrame cool = new KeyFrame(Duration.millis(frameInterval), event -> {
            updateBoard(chilleingandRelaxeing, pixels, moveGenerations);
        });
        boardUpdater.getKeyFrames().add(cool);

        // TODO: during titlescreen the games running, fix later lol :3
        boardUpdater.setCycleCount(Timeline.INDEFINITE);
        boardUpdater.play();

        Timeline manaIncrementor = new Timeline();
        KeyFrame incrementMana = new KeyFrame(Duration.millis(2000), event -> {
            if (moveGenerations) chilleingandRelaxeing.addMana(1);
        });
        manaIncrementor.getKeyFrames().add(incrementMana);
        manaIncrementor.setCycleCount(Timeline.INDEFINITE);
        manaIncrementor.play();


//        Rectangle clipRectangle = new Rectangle(200, 200);
//        manaCircleRadius.setClip(clipRectangle);

        hotbar1.setUserData(15);
        hotbar2.setUserData(3);
        hotbar3.setUserData(3);
        hotbar4.setUserData(10);
        hotbar5.setUserData(35);
        hotbar6.setUserData(10);


        // initialize the player object for mana
        setHeroActions(chilleingandRelaxeing);
        manaLabel.setText(String.format("%d", chilleingandRelaxeing.getMana()));
//        manaLabel.getStylesheets().add(new File())
    }
}