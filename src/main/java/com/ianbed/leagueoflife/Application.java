package com.ianbed.leagueoflife;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public void buildEventListeners(Scene scene)
    {
        scene.addEventFilter(KeyEvent.ANY, keyEvent -> {
            Controller.readKeypress(keyEvent);
        });;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        // Size is set here :3
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        buildEventListeners(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}