package main.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import main.model.Flight.FlightDAO;
import main.model.Flight.FlightModel;

import main.model.Flight.Flight;
import main.model.Observer;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerTraveler implements Observer {
    private FlightDAO flightDAO;
    private ListProperty<List<String>> flightsAsStrings;
    private ListProperty<FlightModel> flights;
    private static StringProperty flightNumber;
    private static StringProperty departureAirport;
    private static StringProperty destinationAirport;


    public ControllerTraveler() {
        flightDAO = new FlightDAO();
        flights = new SimpleListProperty<>(FXCollections.observableArrayList());
        flightsAsStrings = new SimpleListProperty<>(FXCollections.observableArrayList());
        flightNumber = new SimpleStringProperty();
        departureAirport = new SimpleStringProperty();
        destinationAirport = new SimpleStringProperty();


    }
    /*

     */


    /*
     *
     * */

    public void createFlightsTable() {
        List<Flight> flightList = flightDAO.getFlights();
        List<FlightModel> flightModelList = new ArrayList<>();
        for (Flight flight : flightList) {
            flightModelList.add(new FlightModel(flight));
        }
        flights.setAll(flightModelList);
    }

    public void searchFlightBetweenAirports() {
        System.out.println("Departure Airport: " + getDepartureAirport());
        System.out.println("Destination Airport: " + getDestinationAirport());

        List<Flight> flightsList = getFlightService().getFlightsBetweenAirports(
                getDepartureAirport(),
                getDestinationAirport()
        );

        List<FlightModel> FProperties = flightsList.stream().map(FlightModel::new).collect(Collectors.toList());
        flights.setAll(FProperties);
    }

    public void searchFlightByNumber() {
        String flightNumberToSearch = getFlightNumber();
        List<FlightModel> filteredFlights = flights.stream()
                .filter(flightVM -> flightVM.flightNumberProperty().get().equalsIgnoreCase(flightNumberToSearch))
                .collect(Collectors.toList());
        flights.setAll(filteredFlights);
    }

    public void sortByDeparture() {
        fetchFlights();
        List<FlightModel> sortedFProperties = flights.stream()
                .sorted(Comparator.comparing(flightVM -> flightVM.departureAirportProperty().get()))
                .collect(Collectors.toList());
        flights.setAll(sortedFProperties);
    }

    public void sortByDestination() {
        fetchFlights();
        List<FlightModel> sortedFProperties = flights.stream()
                .sorted(Comparator.comparing(flightVM -> flightVM.destinationAirportProperty().get()))
                .collect(Collectors.toList());
        flights.setAll(sortedFProperties);
    }

    public void fetchFlights() { // used for refresh after i do a search!!
        List<Flight> flightsList = flightDAO.getFlights();
        List<FlightModel> FProperties = flightsList.stream().map(FlightModel::new).collect(Collectors.toList());
        flights.setAll(FProperties);
    }

    public ListProperty<FlightModel> getFlightsProperty() {
        return flights;
    }

    public String getFlightNumber() {
        return flightNumber.get();
    }

    public static StringProperty flightNumberProperty() {
        return flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport.get();
    }

    public static StringProperty departureAirportProperty() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport.set(departureAirport);
    }

    public String getDestinationAirport() {
        return destinationAirport.get();
    }

    public static StringProperty destinationAirportProperty() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport.set(destinationAirport);
    }

    public FlightDAO getFlightService() {
        return flightDAO;
    }

    @Override
    public void update() {

    }
}