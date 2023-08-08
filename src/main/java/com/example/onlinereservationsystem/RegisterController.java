package com.example.onlinereservationsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public void handleRegisterButton() throws ClassNotFoundException {
        // Store values from user input
        name = tfName.getText();
        email = tfEmail.getText();
        phoneNumber = tfPhoneNumber.getText();
        password = tfPassword.getText();
        confirmPassword = tfConfirmPassword.getText();

        if(Objects.equals(password, confirmPassword)){
            updateUser();
        } else {
            labelRegistrationMessage.setText("Password mis-match");
        }

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
}
