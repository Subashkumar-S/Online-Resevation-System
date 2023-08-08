package com.example.onlinereservationsystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    public Button btnCheckStatus;
    public AnchorPane ticketBooking;
    public AnchorPane ticketStatusChecking;
    public Label labelUserName;
    public Label labelEmail;
    public ImageView imageUser;
    public Label ticketBookingHeading;
    @FXML
    private ComboBox<String> comboBoxDeparture;
    @FXML
    private ComboBox<String> comboBoxDestination;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField tfTrainNumber;
    @FXML
    private TextField tfTrainName;
    @FXML
    private ComboBox<String> comboBoxClass;
    @FXML
    private TextField tfPassengerName;
    @FXML
    private TableView<TrainsDetails> trainsTable;
    @FXML
    private TableColumn<TrainsDetails, Integer> trainNumber;
    @FXML
    private TableColumn<TrainsDetails, String> trainName;
    @FXML
    private TableColumn<TrainsDetails, String> trainDeparture;
    @FXML
    private TableColumn<TrainsDetails, String> trainDestination;
    @FXML
    private TableColumn<TrainsDetails, String> trainType;
    @FXML
    private TextField tfKeyword;
    @FXML
    private TextField tfAge;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnClose;
    @FXML
    private Label labelBooking;

    ObservableList<TrainsDetails> trainsDetailsObservableList = FXCollections.observableArrayList();
    DatabaseHandler connectNow;
    Connection connectDB;



    public void initialize(Passenger currentUser){

        File userImageFile = new File("assets/user.png");
        Image userImage = new Image(userImageFile.toURI().toString());
        imageUser.setImage(userImage);
        // Display user details 
        labelUserName.setText(currentUser.getUserName());
        labelEmail.setText(currentUser.getEmail());
        // Create new in instance of database
        connectNow = new DatabaseHandler();
        // Establish connection
        connectDB = connectNow.getConnection();
        

        try {
            // Add departure cities to combo box Departure
            List<String> departureCities = getUniqueValuesFromColumn("trains", "departure");
            comboBoxDeparture.getItems().addAll(departureCities);

            // Add destination cities to combo box Destination
            List<String> destinationCities = getUniqueValuesFromColumn("trains", "destination");
            comboBoxDestination.getItems().addAll(destinationCities);

            // Add class of trains to combo box Class
            comboBoxClass.getItems().addAll(Arrays.asList("AC", "Sleeper", "Semi_sleeper", "Unreserved"));

            // To display trains details in Tableview 
            // Fetch details of trains from database
            String trainsViewQuery = "SELECT * FROM trains";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(trainsViewQuery);
            
            while (queryOutput.next()){
                Integer queryNumber = queryOutput.getInt("number");
                String  queryName = queryOutput.getString("name");
                String  queryDeparture = queryOutput.getString("departure");
                String  queryDestination = queryOutput.getString("destination");
                String  queryType = queryOutput.getString("type");

                trainsDetailsObservableList.add(new TrainsDetails(queryNumber, queryName, queryDeparture, queryDestination, queryType));

            }
            
            trainNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
            trainName.setCellValueFactory(new PropertyValueFactory<>("name"));
            trainDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
            trainDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
            trainType.setCellValueFactory(new PropertyValueFactory<>("type"));

            trainsTable.setItems(trainsDetailsObservableList);
            
            // Filtering trains details based on keyword search
            FilteredList<TrainsDetails> filteredData = new FilteredList<>(trainsDetailsObservableList, b -> true);

            tfKeyword.textProperty().addListener((observable, oldValue, newValue) ->{
                filteredData.setPredicate(trainsDetails -> {

                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                        return true;
                    }

                    String searchedKeyword = newValue.toLowerCase();
                    if(trainsDetails.getName().toLowerCase().contains(searchedKeyword)){
                        return true;
                    } else if (trainsDetails.getDeparture().toLowerCase().contains(searchedKeyword)) {
                        return true;
                    } else if (trainsDetails.getDestination().toLowerCase().contains(searchedKeyword)) {
                        return true;
                    } else if (trainsDetails.getType().toLowerCase().contains(searchedKeyword)) {
                        return true;
                    } else{
                        return false;
                    }

                });
            });

            SortedList<TrainsDetails> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(trainsTable.comparatorProperty());
            trainsTable.setItems(sortedData);

        }catch (SQLException e){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    /*
    This method fetches and returns the list of 
    unique values from the column in the database.
     */
    private List<String> getUniqueValuesFromColumn(String tableName, String columnName) throws SQLException {
        List<String> uniqueValues = new ArrayList<>();
        String query = "SELECT DISTINCT " + columnName + " FROM " + tableName;


        try (Statement statement = connectDB.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String value = resultSet.getString(columnName);
                uniqueValues.add(value);
            }
        }

        return uniqueValues;
    }

    public void handleConfirmButton(){
        // Storing all the user input for future use of validation
        //labelBooking.setText("Confirm Button clicked.");
        String trainNumber = tfTrainNumber.getText();
        String trainName = tfTrainName.getText();
        String departure = comboBoxDeparture.getValue();
        String destination = comboBoxDestination.getValue();
        String ticketClass = comboBoxClass.getValue();
        String passengerName = tfPassengerName.getText();
        String age = tfAge.getText();
        LocalDate selectedDate = datePicker.getValue();
        java.sql.Date dateOfJourney = java.sql.Date.valueOf(selectedDate);

        //System.out.println(dateOfJourney);
        
        // Check whether the train details are correct
        boolean correctDetails = validateDetails(trainNumber, trainName, departure, destination);
        //System.out.println("correctDetails is " + correctDetails);
        // If the entered train details not match display error message
        if(!correctDetails){
            labelBooking.setText("Enter correct train details.");
            return;
        }
        
        // check ticket is present or not
        boolean ticketAvailable = checkTicketAvailability(ticketClass, trainNumber);
        System.out.println("ticketAvailable is " + ticketAvailable);
        // If the ticket is not available display error message
        if(!ticketAvailable){
            labelBooking.setText("Ticket Not Available. Select another class.");
            return;
        }
        
        // Check passenger name and age
        if(Objects.equals(passengerName, "") && Objects.equals(age, "")){
            labelBooking.setText("Passenger Name or age cannot be blank.");
            return;
        }
        long pnrNumber = generateTicket(passengerName, age, trainNumber, trainName, departure, destination, dateOfJourney, ticketClass);
        labelBooking.setText("Ticket booked. To check details copy pnr number " + pnrNumber);

    }

    /* This method check whether the ticket available 
    for selected berth and return true
     */
    public boolean checkTicketAvailability(String berth, String trainNumber) {
        try{
            if(Objects.equals(berth, "Unreserved")){
                System.out.println("Ticket booked in " + berth);
                return true;
            }
            String query = "SELECT " + berth + " FROM train WHERE number = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, trainNumber);
            ResultSet resultSet = statement.executeQuery();

            int numberOfTickets = 0;
            if(resultSet.next()){
               numberOfTickets = resultSet.getInt(berth);
               //System.out.println("Ticket available");
            }
            return numberOfTickets > 0;
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        //System.out.println("Ticket not available");
        return false;
    }
    
    /* This method check the train details
    in database and return true or false
     */
    public boolean validateDetails(String trainNumber, String trainName, String departure, String destination){
        String verifyDetails = "SELECT * FROM trains WHERE number = '"+ tfTrainNumber.getText() +"'AND name ='" +tfTrainName.getText() + "'AND departure ='" +comboBoxDeparture.getValue() + "'AND destination ='" +comboBoxDestination.getValue() +  "'";

        try {
            // Execute query
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyDetails);
            System.out.println("verifyDetails is " +queryResult);
            return queryResult.next();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
            return false;
        }

    }
    
    /* This method generate pnr number and generate ticket 
    and store it in the database and gives user pnr number
     */
    public long generateTicket(String passengerName,String  age,String trainNumber,String trainName,String  departure,String  destination, java.sql.Date dateOfJourney, String  ticketClass){
        long uniqueRandomNumber;
        try {

            // generate pnr number
            uniqueRandomNumber = generateUniqueRandomNumber();

            // Update the values to database
            String query = "INSERT INTO tickets (pnr_number, name, age, departure, destination,  date_of_journey, train_number, train_name, class) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setLong(1, uniqueRandomNumber);
            statement.setString(2, passengerName);
            statement.setString(3, age);
            statement.setString(4, departure);
            statement.setString(5, destination);
            statement.setDate(6, dateOfJourney);
            statement.setString(7, trainNumber);
            statement.setString(8, trainName);
            statement.setString(9, ticketClass);

            statement.executeUpdate();

            //  Close the connection
            statement.close();
            connectDB.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uniqueRandomNumber;
    }
    
    /* This method generate
    10 digit unique random number
     */
    public long generateUniqueRandomNumber() {
        Set<Long> generatedNumbers = new HashSet<>();
        Random random = new Random();
        long randomNumber;

        do {
            randomNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        } while (!generatedNumbers.add(randomNumber));

        return randomNumber;
    }
    
    public void handleCloseButton(){
        // on clicking close button exit the application
        Platform.exit();
    }

    public void handleCheckStatusButton() throws IOException {
        // Calling showticketStatus page in HelloApplication
        Passenger user = new Passenger(labelUserName.getText(), labelEmail.getText());
        HelloApplication.showTicketStatusPage(user);
    }
}
