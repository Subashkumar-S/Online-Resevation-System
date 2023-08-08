package com.example.onlinereservationsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public Button btnClose;
    public Label labelLogin;
    public Button btnLogin;
    public Label labelProjectHeading;
    @FXML
    private ImageView imageTrain;
    @FXML
    private ImageView imageUsers;
    @FXML
    private ImageView imageUser;
    @FXML
    private ImageView imagePassword;
    @FXML
    public Label labelLoginMessage;

    @FXML
    public TextField tfUsername;
    @FXML
    public TextField tfPassword;

    @FXML
    public Hyperlink linkSignup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("assets/train.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        imageTrain.setImage(brandingImage);

        File usersImageFile = new File("assets/users.png");
        Image usersImage = new Image(usersImageFile.toURI().toString());
        imageUsers.setImage(usersImage);

        File userImageFile = new File("assets/user.png");
        Image userImage = new Image(userImageFile.toURI().toString());
        imageUser.setImage(userImage);

        File passwordImageFile = new File("assets/password.png");
        Image passwordImage = new Image(passwordImageFile.toURI().toString());
        imagePassword.setImage(passwordImage);
    }

    @FXML
    protected void handleCloseButton() {
        Platform.exit();
    } // Exit the application

    public void handleLoginButton() {
        // Check for blank fields
        if(!tfUsername.getText().isBlank() && !tfPassword.getText().isBlank()){
            // When the fields are not blank validate login
            // Check for valid user
            validateLogin();

        }else{
            labelLoginMessage.setText("Enter Credentials.");
        }

    }

    public void handleSignupLink() {
        try {
            // Get the login stage (window)
            Stage loginStage = (Stage) linkSignup.getScene().getWindow();
            // Close the login page
            loginStage.close();
            // Load the registration FXML file
            Parent registrationRoot = FXMLLoader.load(getClass().getResource("register.fxml"));
            // Create a new scene with the registration content
            Scene registrationScene = new Scene(registrationRoot);
            // Show the registration page
            Stage registrationStage = new Stage();
            registrationStage.initStyle(StageStyle.UNDECORATED);
            registrationStage.setScene(registrationScene);
            registrationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validateLogin() {
        // Create new instance of database
        DatabaseHandler connectNow = new DatabaseHandler();
        // Connect database
        Connection connectDB = connectNow.getConnection();
        // Query to find the user in database
        String verifyLogin = "SELECT * FROM customers WHERE email = '"+ tfUsername.getText() +"'AND password ='" +tfPassword.getText() + "'";

        try {
            // Execute query
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if(queryResult.next()){
                // When valid user enters display

                labelLoginMessage.setText("Login successful. Logging you in!!!");
                System.out.println("Login successful");
                String userName = queryResult.getString("name");
                String email = queryResult.getString("email");
                Passenger user = new Passenger(userName, email);
                HelloApplication.showMainPage(user);


            }else {
                // When invalid user enters display error message
                labelLoginMessage.setText("Invalid login. Try again");
            }

//            return queryResult.next();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}