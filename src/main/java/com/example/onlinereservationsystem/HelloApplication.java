package com.example.onlinereservationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Passenger currentUser;
    static  Stage mainStage;
    static Stage loginStage;
    static Stage registrationStage;

    @Override
    public void start(Stage stage) throws IOException {

        loginStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(scene);
        loginStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    public static void showRegistrationPage() throws IOException {

        loginStage.close();
        registrationStage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Parent root = loader.load();
        registrationStage.initStyle(StageStyle.UNDECORATED);
        registrationStage.setScene(new Scene(root));
        registrationStage.show();

    }

    public static void showLoginPage() throws IOException {

        registrationStage.close();
        loginStage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        Parent root = loader.load();
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(new Scene(root));
        loginStage.show();

    }

    public static void showMainPage(Passenger user) throws IOException {
        currentUser = user;

        loginStage.close();
        mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Parent root = loader.load();
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setScene(new Scene(root));
        mainStage.show();

        // Pass the user to the main controller
        MainController mainController = loader.getController();
        mainController.initialize(currentUser);
    }

    public static void showTicketStatusPage(Passenger user) throws IOException {
        currentUser = user;
        mainStage.close();
        Stage ticketStage = new Stage();
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("status.fxml"));
        Parent root = loader.load();
        ticketStage.initStyle(StageStyle.UNDECORATED);
        ticketStage.setScene(new Scene(root));
        ticketStage.show();

        // Pass the user to the main controller
        StatusController statusController = loader.getController();
        statusController.initialize(currentUser);
    }

}