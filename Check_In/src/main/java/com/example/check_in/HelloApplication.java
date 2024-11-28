package com.example.check_in;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        launch();

        CoinSystem user = new CoinSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 0 to claim coins!");
            int input = scanner.nextInt();

            user.checkIn(input);
            user.displayCoins();
            break;
        }

        user.displayTransactionHistory();
        scanner.close();
    }
}