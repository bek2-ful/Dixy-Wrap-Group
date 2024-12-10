package com.example.prototypes;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.sql.Connection;
import java.util.List;


public class HelloController {
    private DbFunction db = new DbFunction();
    private Connection conn;

    String DBNAME = "gamification";
    String USERNAME = "postgres";
    String PASSWORD = "5133";

    @FXML
    public void initialize() {
        conn = db.connect_to_db(DBNAME, USERNAME, PASSWORD);
        display_vouchers();
        display_points();
        display_transactions();

    }

    @FXML
    private Label current_points;

    private void display_points() {
        current_points.setText(db.read_points(conn,"user_points", 1));
    }



    @FXML
    private GridPane gridPane1;
    private void display_vouchers() {
        List<Voucher> vouchers = db.read_voucher(conn);

        int row = 0;
        int col = 0;

        for (Voucher voucher : vouchers) {
            gridPane1.add(createVoucherVbox(voucher), col, row);

            row++;
        }
    }

    private VBox createVoucherVbox(Voucher voucher) {
        ImageView imageView =  new ImageView(new Image("file:" + voucher.getLogo_path()));
        imageView.setFitHeight(50);
        imageView.setFitWidth(60);
        HBox.setMargin(imageView, new Insets(0,0,0,5));

        VBox details_box = new VBox();
        details_box.setMaxWidth(150);
        details_box.setPadding(new Insets(5));
        details_box.setAlignment(Pos.CENTER_LEFT);


        Button claim_button = new Button("CLAIM");
        claim_button.setPrefWidth(60);
        claim_button.setPrefHeight(30);
        claim_button.setPadding(new Insets(10));
        claim_button.setStyle("-fx-font-size: 11px;" +
                              "-fx-border-color: #cccccc;" +
                              "-fx-font-family: Tahoma;" +
                              "-fx-font-weight: bold;" +
                              "-fx-background-color: linear-gradient(#ffb366, #ff5050);" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;");
        claim_button.setAlignment(Pos.CENTER);
        HBox.setMargin(claim_button, new Insets(0,5,0,0));

        claim_button.setOnMouseEntered(e ->
                claim_button.setStyle("-fx-background-color: linear-gradient(#ff8000, #ff1a1a);" +
                        "-fx-text-fill: black;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;")
        );

        claim_button.setOnMouseExited(e ->
                claim_button.setStyle("-fx-background-color: linear-gradient(#ffb366, #ff5050);" +
                        "-fx-text-fill: black;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;")
        );


        Label nameLabel = new Label (voucher.getReward());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(150);
        nameLabel.setStyle("-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-font-family: Tahoma;");

        Label pointsLabel = new Label("" + voucher.getPoints_needed() + " POINTS");
        pointsLabel.setStyle("-fx-font-size: 14px;" + "-fx-font-family: Tahoma;");

        details_box.getChildren().addAll(nameLabel, pointsLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox_voucher = new HBox();
        hbox_voucher.setSpacing(5);
        hbox_voucher.setAlignment(Pos.CENTER);
        hbox_voucher.getChildren().addAll(imageView, details_box,spacer, claim_button);

        VBox vbox_voucher = new VBox();
//        vbox_voucher.setSpacing(10);
        vbox_voucher.setPrefHeight(90);
        vbox_voucher.setAlignment(Pos.CENTER);
        vbox_voucher.setStyle("-fx-border-color: #cccccc;" +
                              "-fx-background-color: #ffffff;" +
                              "-fx-border-radius: 10;" +
                              "-fx-background-radius: 10;" +
                              "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 2);");
        vbox_voucher.getChildren().add(hbox_voucher);

        claim_button.setOnAction(event -> {
            boolean success = db.redeemVoucher(conn, 1, voucher.getVoucherId(), DBNAME, USERNAME, PASSWORD);
            current_points.setText(db.read_points(conn,"user_points", 1));

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Voucher Claimed");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully redeem " + voucher.getReward() + "!");
                alert.showAndWait();
                display_transactions();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Points");
                alert.setHeaderText(null);
                alert.setContentText("Insufficient Points!");
                alert.showAndWait();
            }
        });

        return vbox_voucher;
    }

    @FXML
    private GridPane gridPane2;
    private void display_transactions() {
        gridPane2.getChildren().clear();
        List<Transaction> transactions = db.read_transaction(conn);

        int row = 0;
        int col = 0;

        for (Transaction transaction : transactions) {
            HBox transactionBox = createTransactionBox(transaction);
            String backgroundColor = (row % 2 == 0) ? "#e8e8e8" : "#f9f9f9";
            transactionBox.setStyle("-fx-background-color: " + backgroundColor + ";");

            gridPane2.add(transactionBox, col, row);


            row++;
        }
    }

    private HBox createTransactionBox(Transaction transaction) {
        VBox details_box = new VBox();
        details_box.setPadding(new Insets(5));
        details_box.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label (transaction.getName());
        nameLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-family: Tahoma;");

        Label dateLabel = new Label(transaction.getDate().toString());
        dateLabel.setStyle("-fx-font-size: 13px;" + "-fx-font-family: Tahoma;");

        details_box.getChildren().addAll(nameLabel, dateLabel);

        Label earned = new Label("+" + Integer.toString(transaction.getPoints_earned()));
        earned.setStyle("-fx-font-size: 20px;" + "-fx-alignment: center;" + "-fx-font-family: Tahoma;");
        earned.setPrefWidth(40);
        HBox.setMargin(earned, new Insets(10, 0, 0,0));


        Label spent = new Label( "-" + Integer.toString(transaction.getPoints_spent()));
        spent.setStyle("-fx-font-size: 20px;" + "-fx-alignment:  center;" + "-fx-font-family: Tahoma;");
        spent.setPrefWidth(50);
        HBox.setMargin(spent, new Insets(10, 10, 0, 0));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox_transaction = new HBox();
        hbox_transaction.setSpacing(10);
        hbox_transaction.setPadding(new Insets(5));
        hbox_transaction.getChildren().addAll(details_box, spacer, earned, spent);

        return hbox_transaction;

    }


    @FXML
    private Button checkInButton;

    @FXML
    protected void checkedInClick() {
        checkInButton.setText("Checked In");
        checkInButton.setDisable(true);

        db.addPoints(conn,1, DBNAME, USERNAME, PASSWORD);
        current_points.setText(db.read_points(conn,"user_points", 1));
        // Call the method to schedule enabling the button at midnight
        scheduleMidnightEnable();
        display_transactions();
        // call the db function that
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

