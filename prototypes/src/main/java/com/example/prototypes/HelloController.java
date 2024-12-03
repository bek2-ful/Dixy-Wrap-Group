package com.example.prototypes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;


public class HelloController {
    private DbFunction db = new DbFunction();
    private Connection conn;

    String DBNAME = "prototype";
    String USERNAME = "bek2";
    String PASSWORD = "1234";

    @FXML
    public void initialize() {
        conn = db.connect_to_db(DBNAME, USERNAME, PASSWORD);
        display_vouchers();
        display_points();

    }

    @FXML
    private Label current_points;

    private void display_points() {
        current_points.setText(db.read_points(conn,"user_points", 1));
    }


    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vbox_voucher;
    @FXML
    private HBox hbox_voucher;
    private void display_vouchers() {
        List<Voucher> vouchers = db.read_voucher(conn);

        int row = 0;
        int col = 0;

        for (Voucher voucher : vouchers) {
            Pane voucherPane = createVoucherPane(voucher);
            gridPane.add(voucherPane, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    private Pane createVoucherPane(Voucher voucher){
        vbox_voucher = new VBox();
        hbox_voucher = new HBox();

        ImageView imageView = new ImageView(new Image("file:" + voucher.getLogo_path()));
        imageView.setFitHeight(85);
        imageView.setFitWidth(50);

        VBox detailsBox = new VBox();
        Label nameLabel = new Label(voucher.getReward());
        Label pointsLabel = new Label("Points: " + voucher.getPoints_needed());
        Label companyLabel = new Label(voucher.getCompany());

        detailsBox.getChildren().addAll(nameLabel, pointsLabel, companyLabel);

        hbox_voucher.getChildren().addAll(imageView, detailsBox);
        vbox_voucher.getChildren().add(hbox_voucher);

        //        pane.setOnMouseClicked(event -> handleVoucherClick(voucher));
        return new Pane(vbox_voucher);
    }

    @FXML
    private Button checkInButton;

    @FXML
    protected void checkedInClick() {
        checkInButton.setText("Checked In");
        checkInButton.setDisable(true);

        Functions add = new Functions();
        add.addPoints(1, DBNAME, USERNAME, PASSWORD);
        current_points.setText(db.read_points(conn,"user_points", 1));
        // Call the method to schedule enabling the button at midnight
        scheduleMidnightEnable();
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

