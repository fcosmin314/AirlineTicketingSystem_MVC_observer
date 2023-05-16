package main.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Flight.Flight;
import main.model.Flight.FlightDAO;
import main.model.Observer;
import main.model.Ticket.Ticket;
import main.model.User.UserDAO;
import main.model.Ticket.TicketDAO;
import main.model.Flight.FlightModel;
import main.model.Ticket.TicketModel;
import main.view.TravelerView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
public class ControllerEmployee implements Observer {

    private FlightDAO flightDAO;
    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    //
    private ListProperty<List<String>> flightsAsStrings;
    private ListProperty<FlightModel> flights;
    private ListProperty<TicketModel> tickets;
    private StringProperty flightNumber;
    private StringProperty departureAirport;
    private StringProperty destinationAirport;
    private StringProperty flightNumberForDeletion;

    private StringProperty departureAirportProperty;
    private StringProperty destinationAirportProperty;
    private StringProperty arrivalTimeProperty;
    private StringProperty departureTimeProperty;
    private StringProperty flightNumberProperty;
    private StringProperty numberOfSeatsProperty;
    private StringProperty priceProperty;

    private StringProperty ticketIdProperty;
    private StringProperty userIdProperty;
    private StringProperty flightIdProperty;
    private ObjectProperty<LocalDateTime> purchaseDateProperty;


    private StringProperty selectedFormat;
    private ObservableList<TicketModel> ticketsTable = FXCollections.observableArrayList();

    private IntegerProperty ticketIdForDeletion;

    private StringProperty usernameProperty;


    public ControllerEmployee(){

        flightDAO = new FlightDAO();
        flights = new SimpleListProperty<>(FXCollections.observableArrayList());
        flightsAsStrings = new SimpleListProperty<>(FXCollections.observableArrayList());
        flightNumber = new SimpleStringProperty();
        ticketDAO = new TicketDAO();
        tickets = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectedFormat = new SimpleStringProperty();
        flightNumberForDeletion = new SimpleStringProperty();
        departureAirportProperty = new SimpleStringProperty();
        destinationAirportProperty = new SimpleStringProperty();
        arrivalTimeProperty = new SimpleStringProperty();
        departureTimeProperty = new SimpleStringProperty();
        flightNumberProperty = new SimpleStringProperty();
        numberOfSeatsProperty = new SimpleStringProperty();
        priceProperty = new SimpleStringProperty();
        //tickets>
        ticketIdForDeletion = new SimpleIntegerProperty(0);
        userIdProperty = new SimpleStringProperty();
        flightIdProperty = new SimpleStringProperty();
        purchaseDateProperty = new SimpleObjectProperty<>();
        ticketIdProperty = new SimpleStringProperty();
        usernameProperty = new SimpleStringProperty();
        flightNumberProperty = new SimpleStringProperty();
        priceProperty = new SimpleStringProperty();
    }

    public void createFlightsTable() {
        List<Flight> flightList = flightDAO.getFlights();
        List<FlightModel> flightModelList = flightList.stream().map(FlightModel::new).collect(Collectors.toList());
        this.getFlightsProperty().setAll(flightModelList);
    }

    public void createTicketsTable() {
        List<Ticket> ticketList = ticketDAO.getAllTickets();
        List<TicketModel> ticketModelList = ticketList.stream().map(TicketModel::new).collect(Collectors.toList());
        this.getTicketsProperty().setAll(ticketModelList);
    }
    public void fetchFlights() { // used for refresh after i do a search!!
        List<Flight> flightsList = flightDAO.getFlights();
        List<FlightModel> FProperties = flightsList.stream().map(FlightModel::new).collect(Collectors.toList());
        flights.setAll(FProperties);
    }

    public void fetchTickets(){
        List<Ticket> ticketsList = ticketDAO.getAllTickets();
        List<TicketModel> TProperies = ticketsList.stream().map(TicketModel::new).collect(Collectors.toList());
        ticketsTable.setAll(TProperies);
    }

    public void searchFlightByNumber(String flightNumberToSearch) {
        List<FlightModel> filteredFlights = this.getFlightsProperty().stream()
                .filter(flightModel -> flightModel.flightNumberProperty().get().equalsIgnoreCase(flightNumberToSearch))
                .collect(Collectors.toList());
        this.getFlightsProperty().setAll(filteredFlights);
    }

    public void switchToTravelerView() {
        TravelerView.show();
    }

    public void deleteFlight(String flightNumber) {
        if (flightNumber != null && !flightNumber.isEmpty()) {
            boolean success = this.getFlightService().deleteFlight(flightNumber);
            if (success) {
                this.fetchFlights();
            }

        }
    }

    public void createFlight() {
        Flight flight = new Flight();
        flight.setDepartureAirport(departureAirportProperty().get());
        flight.setDestinationAirport(destinationAirportProperty().get());
        flight.setArrivalTime(LocalDateTime.parse(arrivalTimeProperty().get()));
        flight.setDepartureTime(LocalDateTime.parse(departureTimeProperty().get()));
        flight.setFlightNumber(flightNumberProperty().get());
        flight.setNumberOfSeats(Integer.parseInt(numberOfSeatsProperty().get()));
        flight.setPrice(Double.parseDouble(priceProperty().get()));
        boolean success = flightDAO.createFlight(flight);
        if (success) {
            fetchFlights();
        } else {
        }
    }

    public void updateFlight() {
        System.out.println("Executing update command");
        Flight flight = new Flight();
        flight.setDepartureAirport(departureAirportProperty().get());
        flight.setDestinationAirport(destinationAirportProperty().get());
        flight.setArrivalTime(LocalDateTime.parse(arrivalTimeProperty().get()));
        flight.setDepartureTime(LocalDateTime.parse(departureTimeProperty().get()));
        flight.setFlightNumber(flightNumberProperty().get());
        flight.setNumberOfSeats(Integer.parseInt(numberOfSeatsProperty().get()));
        flight.setPrice(Double.parseDouble(priceProperty().get()));

        boolean success = flightDAO.updateFlight(flight);
        if (success) {
            fetchFlights();
        } else {
        }
    }

    public void saveFlightsAsData() {
        fetchFlights();

        String format = selectedFormatProperty().get();
        if (format != null && !format.isEmpty()) {
            flightDAO.saveFlightsToFile(format);
        } else {
            System.out.println("No format selected.");
        }

    }

    public void createTicket() {
        Ticket ticket = new Ticket();
        ticket.setUserId(userIdProperty().get());
        ticket.setFlightId(flightIdProperty().get());
        ticket.setPurchaseDate(LocalDateTime.now());

        double price = flightDAO.getFlightPrice(ticket.getFlightId());
        System.out.println("Fetched price: " + price);
        ticket.setPrice(price);

        boolean success = ticketDAO.createTicket(ticket);
        if (success) {
            fetchTickets();
            // remove the newly created ticket from the tickets list
            tickets.removeIf(ticketVM -> ticketVM.idProperty().get().equals(String.valueOf(ticket.getId())));
        }
    }

    public void deleteTicket() {
        int ticketId = ticketIdForDeletionProperty().get();
        boolean success = ticketDAO.deleteTicketById(ticketId);

        if (success) {
            // remove the deleted ticket from the tickets list
            tickets.removeIf(ticketVM -> ticketVM.idProperty().get().equals(String.valueOf(ticketId)));
        }
    }

    public void updateTicket() {
        Ticket ticket = new Ticket();
        ticket.setUserId(userIdProperty().get());
        ticket.setFlightId(flightIdProperty().get());
        ticket.setPurchaseDate(LocalDateTime.now());

        double price = flightDAO.getFlightPrice(ticket.getFlightId());
        System.out.println("Fetched price: " + price); //deb
        ticket.setPrice(price);

        boolean success = ticketDAO.createTicket(ticket);
        if (success) {
            fetchTickets(); //refresh
        } else {//todo
        }
    }

    public IntegerProperty ticketIdForDeletionProperty() {
        return ticketIdForDeletion;
    }
    public void setTicketsTable(ObservableList<TicketModel> ticketsTable) {
        this.ticketsTable = ticketsTable;
    }
    public StringProperty flightNumberForDeletionProperty() {
        return flightNumberForDeletion;
    }
    public StringProperty departureAirportProperty() { return departureAirportProperty; }
    public StringProperty destinationAirportProperty() { return destinationAirportProperty; }
    public StringProperty arrivalTimeProperty() { return arrivalTimeProperty; }
    public StringProperty departureTimeProperty() { return departureTimeProperty; }

    public StringProperty numberOfSeatsProperty() { return numberOfSeatsProperty; }
    public StringProperty priceProperty() { return priceProperty; }
    public StringProperty selectedFormatProperty() {
        return selectedFormat;
    }

    public ListProperty<FlightModel> getFlightsProperty() {
        return flights;
    }

    public ListProperty<TicketModel> getTicketsProperty() {
        return tickets;
    }

    public String getFlightNumber() {
        return flightNumber.get();
    }

    public StringProperty flightNumberProperty() {
        return flightNumber;
    }

    public FlightDAO getFlightService() {
        return flightDAO;
    }

    public ObservableList<TicketModel> getTickets() {
        return tickets;
    }
    public TicketDAO getTicketService() {
        return ticketDAO;
    }

    public StringProperty userIdProperty() {
        return userIdProperty;
    }

    public StringProperty getTicketIdProperty(){
        return ticketIdProperty;
    }
    public StringProperty flightIdProperty() {
        return flightIdProperty;
    }

    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    public ObjectProperty<LocalDateTime> purchaseDateProperty() {
        return purchaseDateProperty;
    }

    public UserDAO getUserService() {
        return userDAO;
    }

    @Override
    public void update() {

    }
}
