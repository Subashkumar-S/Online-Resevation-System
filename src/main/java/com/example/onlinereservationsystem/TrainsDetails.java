package com.example.onlinereservationsystem;

public class TrainsDetails {
    Integer number;
    String name, departure, destination, type;

    public TrainsDetails(Integer number, String name, String departure, String destination, String type){
        this.number = number;
        this.name = name;
        this.departure = departure;
        this.destination =destination;
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
