package com.example.prototypes;

import java.util.List;
import java.util.ArrayList;

import java.lang.ClassNotFoundException;
import java.lang.IndexOutOfBoundsException;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;

import javax.sql.rowset.CachedRowSet;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {

    public static HelloApplication me;
    public static Stage stage;
    private Socket clientSocket = null;
    private String userCommand = null;
    private CachedRowSet serviceOutcome = null;

    @FXML
    private Label currentPoints;

    @FXML
    private TableView <MyTransaction> transaction;

    public class MyTransaction {
        private StringProperty date;
        private StringProperty name;
        private StringProperty amount;

        public void setDate (String value) {
            dateProperty().set(value);
        }
        public String getDate() {
            return dateProperty().get();
        }
        public void setName(String value) {
            nameProperty().set(value);
        }
        public String getName() {
            return nameProperty().get();
        }
        public void setAmount(String value) {
            amountProperty().set(value);
        }
        public String getAmount() {
            return amountProperty().get();
        }


        public StringProperty dateProperty() {
            if (date == null)
                date = new SimpleStringProperty(this, "");
            return date;
        }
        public StringProperty nameProperty() {
            if (name == null)
                name = new SimpleStringProperty(this, "");
            return name;
        }
        public StringProperty amountProperty() {
            if (amount == null)
                amount = new SimpleStringProperty(this, "");
            return amount;
        }

    }


    public HelloApplication() {
        me = this;
    }

    public void initializeSocket() {

        try{
            clientSocket = new Socket(Credentials.HOST, Credentials.PORT);
        }catch (UnknownHostException e){
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
    }


    // but we're not requesting anything so not sure if needed or not
    public void requestService() {
        try {
            System.out.println(this.userCommand);

            OutputStream requestStream = this.clientSocket.getOutputStream();
            OutputStreamWriter requestStreamWriter = new OutputStreamWriter(requestStream);

            requestStreamWriter.write(userCommand + "#");
            requestStreamWriter.flush();

        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }
    }

    public void reportServiceOutcome() {
        try {

            String tmp = "";
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(clientSocket.getInputStream());
            serviceOutcome = (CachedRowSet) outcomeStreamReader.readObject();

            Scene scene = stage.getScene();
            Parent root = scene.getRoot();
            ObservableList<Node> children = root.getChildrenUnmodifiable();

            TableView<MyTransaction> tmpRecords = null;
            for (Node child : children) {
                if (child instanceof TableView) {
                    tmpRecords = (TableView<MyTransaction>) child;
                    break;
                }
            }
            if (tmpRecords == null) {
                System.out.println("Suitable TableView not found");
            } else {
                tmpRecords.getItems().clear();

                while (serviceOutcome.next()){
                    MyTransaction record = new MyTransaction();
                    record.setDate(serviceOutcome.getString("title"));
                    record.setName(serviceOutcome.getString("label"));
                    record.setAmount(serviceOutcome.getString("genre"));

                    tmpRecords.getItems().add(record);
                }
            }

            StringBuilder tb = new StringBuilder();
            for (MyTransaction record : tmpRecords.getItems()) {
                tb.append(record.getDate()).append(" | ").append(record.getName()).append(" | ").append(record.getAmount()).append("\n");
            }
            tmp = tb.toString();
            System.out.println(tmp +"\n====================================\n");


        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }catch(ClassNotFoundException e){
            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
        }catch(SQLException e){
            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
        }

    }

    public void execute() {

        try{

            //Initializes the socket
            this.initializeSocket();

            //Request service
            this.requestService();

            //Report user outcome of service
            this.reportServiceOutcome();

            //Close the connection with the server
            this.clientSocket.close();

        }catch(Exception e)
        {// Raised if connection is refused or other technical issue
            System.out.println("Client: Exception " + e);
        }
    }







    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 640);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}