package com.example.prototypes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.Calendar;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button checkInButton;

    @FXML
    private VBox voucherBox;

    @FXML
    private Pane voucherPane;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void checkedInClick() {
        checkInButton.setText("Checked In");
        checkInButton.setDisable(true);
        // Call the method to schedule enabling the button at midnight
        scheduleMidnightEnable();
    }

    private void scheduleMidnightEnable() {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate how many milliseconds until midnight
        long midnightMillis = getMillisUntilMidnight(currentTimeMillis);

        // Schedule the task to re-enable the button at midnight
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(midnightMillis), event -> {
            checkInButton.setDisable(false);  // Enable the button at midnight
        }));
        timeline.play();
    }

    private long getMillisUntilMidnight(long currentTimeMillis) {
        // Get the current time in hours, minutes, and seconds
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);

        // Set the calendar time to midnight
        calendar.add(java.util.Calendar.DATE, 1);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);

        // Calculate the difference between now and midnight
        return calendar.getTimeInMillis() - currentTimeMillis;
    }

    @FXML
    protected void onVoucherClick (MouseEvent event) {

        // check first if they have enough points to claim the voucher

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Claim Voucher");
        alert.setHeaderText("Do you want to claim this voucher?");
        alert.setContentText("You are about to claim the voucher");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                claimVoucher();
            }
        });
    }

    private void claimVoucher() {
        System.out.println("Voucher claimed successfully!");
    }

}

