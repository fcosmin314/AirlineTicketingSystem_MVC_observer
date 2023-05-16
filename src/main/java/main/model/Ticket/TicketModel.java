package main.model.Ticket;

import javafx.beans.property.*;
import main.model.AbstractObserver;

import java.time.LocalDateTime;

public class TicketModel extends AbstractObserver {
    private Ticket ticket;
    private StringProperty id;
    private StringProperty userId;
    private StringProperty flightId;
    private ObjectProperty<LocalDateTime> purchaseDate;
    private StringProperty username;
    private StringProperty flightNumber;
    private DoubleProperty price;
    public TicketModel(Ticket ticket) {
        this.ticket = ticket;
        this.id = new SimpleStringProperty(String.valueOf(ticket.getId()));
        this.userId = new SimpleStringProperty(String.valueOf(ticket.getUserId()));
        this.flightId = new SimpleStringProperty(String.valueOf(ticket.getFlightId()));
        this.purchaseDate = new SimpleObjectProperty<>(ticket.getPurchaseDate());
        this.username = new SimpleStringProperty(ticket.getUsername());
        this.flightNumber = new SimpleStringProperty(ticket.getFlightNumber());
        this.price = new SimpleDoubleProperty(ticket.getPrice());
        notifyObservers();
    }
    public StringProperty idProperty() {
        return id;
    }
    public ObjectProperty<LocalDateTime> purchaseDateProperty() {
        return purchaseDate;
    }
    public StringProperty usernameProperty() {
        return username;
    }
    public StringProperty flightNumberProperty() {
        return flightNumber;
    }
    public DoubleProperty pricePropertyTicket() {
        return price;
    }

}
