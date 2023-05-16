package main.model.Flight;

import javafx.beans.property.*;
import main.model.AbstractObserver;

import java.time.LocalDateTime;

public class FlightModel extends AbstractObserver {
    private Flight flight;
    private StringProperty departureAirport;
    private StringProperty destinationAirport;
    private StringProperty flightNumber;
    private ObjectProperty<LocalDateTime> departureTime;
    private ObjectProperty<LocalDateTime> arrivalTime;
    private DoubleProperty price;
    private IntegerProperty numberOfSeats;

    public FlightModel(Flight flight) {
        this.flight = flight;
        departureAirport = new SimpleStringProperty(flight.getDepartureAirport());
        destinationAirport = new SimpleStringProperty(flight.getDestinationAirport());
        flightNumber = new SimpleStringProperty(flight.getFlightNumber());
        departureTime = new SimpleObjectProperty<>(flight.getDepartureTime());
        arrivalTime = new SimpleObjectProperty<>(flight.getArrivalTime());
        price = new SimpleDoubleProperty(flight.getPrice());
        numberOfSeats = new SimpleIntegerProperty(flight.getNumberOfSeats());
        notifyObservers();
    }



    public StringProperty departureAirportProperty() {
        return departureAirport;
    }

    public StringProperty destinationAirportProperty() {
        return destinationAirport;
    }

    public StringProperty flightNumberProperty() {
        return flightNumber;
    }

    public ObjectProperty<LocalDateTime> departureTimeProperty() {
        return departureTime;
    }

    public ObjectProperty<LocalDateTime> arrivalTimeProperty() {
        return arrivalTime;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty numberOfSeatsProperty() {
        return numberOfSeats;
    }

}