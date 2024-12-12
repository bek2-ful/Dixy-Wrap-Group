package com.example.prototypes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 640);
        stage.setTitle("SSH APP - Points Page");
        stage.setScene(scene);

        // This is set to false to mimic phone's size

        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
       launch();
    }
}