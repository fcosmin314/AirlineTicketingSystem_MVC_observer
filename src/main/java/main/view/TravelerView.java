package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controller.ControllerTraveler;
import main.model.Flight.FlightModel;
import main.controller.utils.LanguageManager;
import main.model.Observer;

import java.io.IOException;

public class TravelerView implements Observer {
    @FXML
    private Button searchBetweenAirportsButton;
    @FXML
    private Button searchFlightNumButton;
    @FXML
    private Button sortByDepartureButton;
    @FXML
    private Button sortByDestinationButton;
    @FXML
    private Label viewFlightBetweenLabel;
    @FXML
    private Label availableFlightsLabel;
    @FXML
    private TableView<FlightModel> flightTableView;
    @FXML
    private TableColumn<FlightModel, String> departureColumn;
    @FXML
    private TableColumn<FlightModel, String> destinationColumn;
    @FXML
    private TableColumn<FlightModel, String> flightNumber;
    @FXML
    private TableColumn<FlightModel, String> departureTime;
    @FXML
    private TableColumn<FlightModel, String> arrivalTime;
    @FXML
    private TableColumn<FlightModel, Double> priceFlight;
    @FXML
    private TableColumn<FlightModel, Integer> emptySeats;
    @FXML
    private TextField searchFlightNumber;
    @FXML
    private TextField searchByDeparture;
    @FXML
    private TextField searchByDestination;

    private final ControllerTraveler travelerController;

    public TravelerView() {
        travelerController = new ControllerTraveler();
        LanguageManager.getInstance().addObserver(this);
    }
    public static void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TravelerView.class.getResource("/main/traveler-view.fxml"));
            Parent root = fxmlLoader.load();

            TravelerView travelerView = fxmlLoader.getController();
            travelerView.initialize();

            Stage travelerStage = new Stage();
            travelerStage.initModality(Modality.APPLICATION_MODAL);
            travelerStage.setTitle("Traveler View");
            travelerStage.setScene(new Scene(root));
            travelerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        departureColumn.setCellValueFactory(cellData -> cellData.getValue().departureAirportProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationAirportProperty());
        flightNumber.setCellValueFactory(cellData -> cellData.getValue().flightNumberProperty());
        departureTime.setCellValueFactory(cellData -> cellData.getValue().departureTimeProperty().asString());
        arrivalTime.setCellValueFactory(cellData -> cellData.getValue().arrivalTimeProperty().asString());
        priceFlight.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        emptySeats.setCellValueFactory(cellData -> cellData.getValue().numberOfSeatsProperty().asObject());

        searchFlightNumber.textProperty().bindBidirectional(ControllerTraveler.flightNumberProperty());
        searchByDeparture.textProperty().bindBidirectional(ControllerTraveler.departureAirportProperty());
        searchByDestination.textProperty().bindBidirectional(ControllerTraveler.destinationAirportProperty());



        travelerController.createFlightsTable();
        flightTableView.itemsProperty().bind(travelerController.getFlightsProperty());
        update();
    }
    @FXML
    private void onSortByDestinationClicked() {
        travelerController.sortByDestination();
    }

    @FXML
    private void onSortByDepartureClicked() {
        travelerController.sortByDeparture();
    }

    @FXML
    private void onSearchByFlightNumberClicked() {
        travelerController.searchFlightByNumber();
    }

    @FXML
    private void onSearchByDepartureDestination() {
        travelerController.searchFlightBetweenAirports();
    }

    @Override
    public void update() {
        availableFlightsLabel.setText(LanguageManager.getInstance().getText("availableFlightsLabel"));
        departureColumn.setText(LanguageManager.getInstance().getText("departureColumn"));
        destinationColumn.setText(LanguageManager.getInstance().getText("destinationColumn"));
        flightNumber.setText(LanguageManager.getInstance().getText("flightNumber"));
        viewFlightBetweenLabel.setText(LanguageManager.getInstance().getText("viewFlightBetweenLabel"));
        departureTime.setText(LanguageManager.getInstance().getText("departureTime"));
        arrivalTime.setText(LanguageManager.getInstance().getText("arrivalTime"));
        priceFlight.setText(LanguageManager.getInstance().getText("priceFlight"));
        emptySeats.setText(LanguageManager.getInstance().getText("emptySeats"));
        searchFlightNumber.setPromptText(LanguageManager.getInstance().getText("searchFlightNumber"));
        searchBetweenAirportsButton.setText(LanguageManager.getInstance().getText("onSearchByDepartureDestination"));
        searchFlightNumButton.setText(LanguageManager.getInstance().getText("onSearchByFlightNumberClicked"));
        sortByDepartureButton.setText(LanguageManager.getInstance().getText("onSortByDepartureClicked"));
        sortByDestinationButton.setText(LanguageManager.getInstance().getText("onSortByDestinationClicked"));
        searchByDeparture.setPromptText(LanguageManager.getInstance().getText("searchByDeparture"));
        searchByDestination.setPromptText(LanguageManager.getInstance().getText("searchByDestination"));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LanguageManager.getInstance().removeObserver(this);
    }
}