package com.ianbed.leagueoflife;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public void buildEventListeners(Scene scene)
    {
        scene.addEventFilter(KeyEvent.ANY, keyEvent -> {
            Controller.readKeyPress(keyEvent);
        });;

        scene.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            Controller.readMousePress(mouseEvent);
        });;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        // Size is set here :3
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000); // try getting 1000x800 later maybe?
        stage.setTitle("League of Life");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        buildEventListeners(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}