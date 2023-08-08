package com.example.onlinereservationsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StatusController {
    public Label labelTicketStatusMessage;
    public ImageView imageUser;
    public Label labelTicketStatusHeading;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelUserName;
    @FXML
    private TextField tfPnrNumber;

    @FXML
    private Button btnViewDetails;
    @FXML
    private Button btnCancelTicket;
    @FXML
    private Button btnClose;

    @FXML
    private TableView<TicketDetailsTable> tableTicketDetails;

    @FXML
    private TableColumn<TicketDetailsTable, String> columnNameColumn;

    @FXML
    private TableColumn<TicketDetailsTable, Object> valueColumn;

    TicketDetails ticket;
    DatabaseHandler connectNow;
    Connection connectDB;
    public void initialize(Passenger currentUser) {
        File userImageFile = new File("assets/user.png");
        Image userImage = new Image(userImageFile.toURI().toString());
        imageUser.setImage(userImage);
        // Display user details
        labelUserName.setText(currentUser.getUserName());
        labelEmail.setText(currentUser.getEmail());
    }


    public void handleViewDetailsButton(){
        // Storing pnr number from user
        long pnrNumber = Long.parseLong(tfPnrNumber.getText());
        if(pnrNumber == 0){
            return;
        }
        // Creating a object of Ticket details
        TicketDetails ticketDetails = getTicketDetailsByPNR(pnrNumber);
        if(ticketDetails == null){
            labelTicketStatusMessage.setText("Ticket not available");
            return;
        }
        //System.out.println("passenger name :" + ticketDetails.getPassengerName());
        displayTicketsInTable(ticketDetails);


    }

    /* This method get ticket details from database
    and store it in object
     */
    public TicketDetails getTicketDetailsByPNR(long pnrNumber) {


        try {
            // Create new instance of database
            connectNow = new DatabaseHandler();
            // Connect database
            connectDB = connectNow.getConnection();
            String query = "SELECT * FROM tickets WHERE pnr_number = ?";
            PreparedStatement stmt = connectDB.prepareStatement(query);
            stmt.setLong(1, pnrNumber);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                // If the PNR number is found in the database, create a Ticket object
                    ticket = new TicketDetails();
                    ticket.setPnrNumber(resultSet.getLong("pnr_number"));
                    ticket.setPassengerName(resultSet.getString("name"));
                    ticket.setPassengerAge(resultSet.getInt("age"));
                    ticket.setTrainName(resultSet.getString("train_name"));
                    ticket.setTrainNumber(resultSet.getString("train_number"));
                    ticket.setBerth(resultSet.getString("class"));
                    ticket.setDateOfJourney(String.valueOf(resultSet.getDate("date_of_journey").toLocalDate()));
                    ticket.setDeparture(resultSet.getString("departure"));
                    ticket.setDestination(resultSet.getString("destination"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticket;
    }

    /* This method display the ticket details
    in the table. So that the user can verify
     */
    public void displayTicketsInTable(TicketDetails ticketDetails){

        List<TicketDetailsTable> ticketList = new ArrayList<>();
        ticketList.add(new TicketDetailsTable("PNR Number", ticketDetails.getPnrNumber()));
        ticketList.add(new TicketDetailsTable("Passenger Name", ticketDetails.getPassengerName()));
        ticketList.add(new TicketDetailsTable("Passenger Age", ticketDetails.getPassengerAge()));
        ticketList.add(new TicketDetailsTable("Date of journey", ticketDetails.getDateOfJourney()));
        ticketList.add(new TicketDetailsTable("Train Number", ticketDetails.getTrainNumber()));
        ticketList.add(new TicketDetailsTable("Train Name", ticketDetails.getTrainName()));
        ticketList.add(new TicketDetailsTable("Class", ticketDetails.getBerth()));
        ticketList.add(new TicketDetailsTable("Departure", ticketDetails.getDeparture()));
        ticketList.add(new TicketDetailsTable("Destination", ticketDetails.getDestination()));

        columnNameColumn.setCellValueFactory(new PropertyValueFactory<>("columnName"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("columnValue"));

        // Populate the TableView with data from the ticket details objects
        tableTicketDetails.getItems().addAll(ticketList);
    }



    /*
    This method cancel the ticket i.e. delete the ticket
    from the database and display message to user
     */
    public void handleCancelTicketButton(){

        // Getting ticket details from user
        if(Objects.equals(tfPnrNumber.getText(), "")){
            labelTicketStatusMessage.setText("Enter PNR Number to delete ticket.");
            return;
        }

        // Show a confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Cancel Ticket");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to cancel the ticket?");

        // Handle the user's response
        // If the user confirm to cancel the ticket
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Perform cancellation and update table view
                boolean ticketDeleted = deleteTicket();
                if (ticketDeleted){
                    labelTicketStatusMessage.setText("Ticket Cancelled");
                    tableTicketDetails.setItems(null);

                    // Show a success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Cancellation Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The ticket has been successfully canceled.");
                    successAlert.showAndWait();
                    return;
                }

                // Display error when ticket details not match in database
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Cancellation Error");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Enter valid ticket.");
                successAlert.showAndWait();


            }
        });
    }
    /*
    This method is used to delete the
    ticket from the database
     */
    public boolean deleteTicket(){
        long pnrNumber = Long.parseLong(tfPnrNumber.getText());
        boolean deleted = false;

        try {
            // Create new instance of database
            connectNow = new DatabaseHandler();
            // Connect database
            connectDB = connectNow.getConnection();
            String query = "DELETE FROM tickets WHERE pnr_number = ?";
            PreparedStatement stmt = connectDB.prepareStatement(query);
            stmt.setLong(1, pnrNumber);
            int rowsAffected = stmt.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }
    public void handleCloseButton(){
        Platform.exit();
    }

}
