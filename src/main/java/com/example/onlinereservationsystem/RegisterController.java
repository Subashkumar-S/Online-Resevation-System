package com.example.onlinereservationsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterController {

    @FXML
    public Label labelRegistrationMessage;
    public Label labelName;
    public TextField tfName;
    public Label labelEmail;
    public TextField tfEmail;
    public Label labelPhoneNumber;
    public TextField tfPhoneNumber;
    public Label labelPassword;
    public Label labelConfirmPassword;
    public TextField tfConfirmPassword;
    public Button btnRegister;
    public Button btnClose;
    public Hyperlink linkLogin;
    public PasswordField tfPassword;
    public Label labelRegistrationHeading;

    String name;
    String email;
    String phoneNumber;
    String password;
    String confirmPassword;
    public void handleCloseButton(){
        Platform.exit();
    }

    public void handleRegisterButton()  {
        // Store values from user input
        name = tfName.getText();
        email = tfEmail.getText();
        phoneNumber = tfPhoneNumber.getText();
        password = tfPassword.getText();
        confirmPassword = tfConfirmPassword.getText();
        // Check all fields are not empty
        if (Objects.equals(name, "") || Objects.equals(email, "")  || Objects.equals(phoneNumber, "") || Objects.equals(password, "") || Objects.equals(confirmPassword, "")){
            labelRegistrationMessage.setText("Enter all the fields");
            return;
        }
        boolean userAlreadyExists = isUsernameExists(email);
        //System.out.println( "userAlreadyExists is "+ userAlreadyExists);
        if (userAlreadyExists) {
            labelRegistrationMessage.setText("Email id already resitered.");
            return;
        }

        if(Objects.equals(password, confirmPassword)){
            updateUser();
        } else {
            labelRegistrationMessage.setText("Password mis-match");
        }

    }

    public boolean isUsernameExists(String username) {
        boolean isExists = false;

        try {
            // Connect to database
            DatabaseHandler connectNow = new DatabaseHandler();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT COUNT(*) FROM customers WHERE email = ?";

            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                isExists = (count > 0);
            }

            resultSet.close();
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isExists;
    }

    public void updateUser(){
        // Connect to database
        DatabaseHandler connectNow = new DatabaseHandler();
        Connection connectDB = connectNow.getConnection();

        try {
            // Update the values to database
            String query = "INSERT INTO customers (name, email, phoneNumber, password) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phoneNumber);
            statement.setString(4, password);

            statement.executeUpdate();

            //  Close the connection
            statement.close();
            connectDB.close();
        } catch (SQLException e) {

            throw new RuntimeException(e);

        }
        labelRegistrationMessage.setText("Registration successful! Login now.");
    }


    public void handleLoginLink() {
        try {
            HelloApplication.showLoginPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
