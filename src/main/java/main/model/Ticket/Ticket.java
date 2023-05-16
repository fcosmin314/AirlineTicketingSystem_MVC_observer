package main.model.Ticket;

import main.model.Flight.Flight;
import main.model.User.User;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    public void setId(int id) {
        this.id = id;
    }
    private int userId;
    private int flightId;
    private LocalDateTime purchaseDate;
    private User user;
    private Flight flight;

    private double priceTicket;

    public Ticket(){
        this.id = getId();
       // this.price = getPrice();
    }
    public Ticket(int id, int userId, int flightId, LocalDateTime purchaseDate) {
        this.id = id;
        this.userId = userId;
        this.flightId = flightId;
        this.purchaseDate = purchaseDate;
    }
    public Ticket(int id, int userId, int flightId, LocalDateTime purchaseDate, User user, Flight flight) {
        this.id = id;
        this.userId = userId;
        this.flightId = flightId;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.flight = flight;
    }

    public Ticket(int userId, int flightId, LocalDateTime purchaseDate, User user, Flight flight) {
        this.userId = userId;
        this.flightId = flightId;
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.flight = flight;
    }

    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public int getFlightId() {
        return flightId;
    }
    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getFlightNumber() {
        return flight.getFlightNumber();
    }
    public double getPrice() {
        return flight.getPrice();
    }

    public void setPurchaseDate(LocalDateTime parse) {
        this.purchaseDate = parse;
    }

    public void setUserId(String s) {
        this.userId = Integer.parseInt(s);
    }

    public void setFlightId(String s) {
        this.flightId = Integer.parseInt(s);
    }

    public void setPrice(double parseDouble) {
        this.priceTicket = parseDouble;
    }
}