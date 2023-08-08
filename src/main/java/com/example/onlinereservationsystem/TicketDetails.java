package com.example.onlinereservationsystem;

public class TicketDetails {
    private long pnrNumber;
    private String passengerName;
    private Integer passengerAge;
    private String departure;
    private String destination;
    private String dateOfJourney;
    private String trainNumber;
    private String trainName;
    private String berth;

    public TicketDetails(){

    }

    public TicketDetails(long pnrNumber ,String passengerName, Integer passengerAge, String departure, String destination, String dateOfJourney, String trainNumber, String trainName, String berth){
        this.pnrNumber = pnrNumber;
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.departure = departure;
        this.destination =destination;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.dateOfJourney = dateOfJourney;
        this.berth = berth;
    }

    public long getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(long pnrNumber) {
        this.pnrNumber = pnrNumber;
    }
    public String getPassengerName() {
        return passengerName;
    }


    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(Integer passengerAge) {
        this.passengerAge = passengerAge;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }
}
