package main.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import main.controller.ControllerEmployee;
import main.model.Flight.FlightModel;
import main.controller.utils.LanguageManager;
import main.model.Ticket.TicketModel;
import main.model.Observer;

import java.io.IOException;


public class EmployeeView implements Observer {
    public TextField searchByFlightNumberText;
    public Button searchFlightButton;
    public ChoiceBox saveFlightDataAsChoiceBox;
    public Button switchToTravelerButton;
    public TextField selectFlightForDeletionText;
    public TextField changeDepartureAirportText;
    public TextField changeDestinationAirportText;
    public TextField changeArrivalTimeText;
    public TextField changeDepartureTimeText;
    public TextField changeFlightNumberText;
    public TextField changeNoOfSeatsText;
    public TextField changePriceText;
    public Label ticketsControlLabel;
    public Label priceLabel;
    public Label numberOfSeatsLabel;
    public Label ticketIdLabel;
    public Button viewStatisticsButton;
    @FXML
    private Label flightIdLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label flightNumberLabel;
    @FXML
    private Label arrivalTimeLabel;
    @FXML
    private Label departureAirportLabel;
    @FXML
    private Label destinationAirportLabel;
    @FXML
    private Label departureTimeLabel;
    @FXML
    private Button saveFlightDataAsFile;
    @FXML
    private Label flightsControlLabel;
    private ControllerEmployee employeeController;
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
    public Button deleteFlightButton;
    @FXML
    public Button addFlightButton;
    @FXML
    public Button updateFlightButton;

    // Ticket TableView and its columns
    @FXML private TableView<TicketModel> ticketTableView;
    @FXML
    public TableColumn<TicketModel, String> ticketIdColumn;
    @FXML private TableColumn<TicketModel, String> usernameTicketColumn;
    @FXML private TableColumn<TicketModel, String> flightNumberTicketColumn;
    @FXML private TableColumn<TicketModel, String> purchaseDateTicketColumn;
    @FXML private TableColumn<TicketModel, String> flightPriceTicketColumn;


    // TextFields for updating ticket information
    @FXML private TextField changeUserClientTicketText;
    @FXML private TextField changeFlightNumberTicketText;
    @FXML
    public TextField changeTicketIdText;

    // Buttons for ticket operations
    @FXML private Button addTicketButton;
    @FXML private Button deleteTicketButton;
    @FXML private Button updateTicketButton;

    // TextField for selecting a ticket for deletion
    @FXML private TextField selectTicketForDeletionText;
    public EmployeeView(){
        employeeController = new ControllerEmployee();
        LanguageManager.getInstance().addObserver(this);
    }
//
    public static void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EmployeeView.class.getResource("/main/employee-view.fxml"));
            Parent root = fxmlLoader.load();

            ControllerEmployee employeeController = new ControllerEmployee();
            EmployeeView employeeView = fxmlLoader.getController();
            employeeView.initialize();

            Stage employeeStage = new Stage();
            employeeStage.initModality(Modality.APPLICATION_MODAL);
            employeeStage.setTitle("Employee View");
            employeeStage.setScene(new Scene(root));
            employeeStage.show();
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
        searchByFlightNumberText.textProperty().bindBidirectional(employeeController.flightNumberProperty());
        saveFlightDataAsChoiceBox.getItems().addAll("csv", "json", "xml", "txt");
        saveFlightDataAsChoiceBox.valueProperty().bindBidirectional(employeeController.selectedFormatProperty());
        //
        selectFlightForDeletionText.textProperty().bindBidirectional(employeeController.flightNumberForDeletionProperty());
        //
        changeDepartureAirportText.textProperty().bindBidirectional(employeeController.departureAirportProperty());
        changeDestinationAirportText.textProperty().bindBidirectional(employeeController.destinationAirportProperty());
        changeArrivalTimeText.textProperty().bindBidirectional(employeeController.arrivalTimeProperty());
        changeDepartureTimeText.textProperty().bindBidirectional(employeeController.departureTimeProperty());
        changeFlightNumberText.textProperty().bindBidirectional(employeeController.flightNumberProperty());
        changeNoOfSeatsText.textProperty().bindBidirectional(employeeController.numberOfSeatsProperty());
        changePriceText.textProperty().bindBidirectional(employeeController.priceProperty());
        addFlightButton.setOnAction(event -> onAddFlightButtonClicked(event));
        updateFlightButton.setOnAction(event -> onUpdateFlightButtonClicked(event));


        //for tickets>
        ticketIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        usernameTicketColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        flightNumberTicketColumn.setCellValueFactory(cellData -> cellData.getValue().flightNumberProperty());
        purchaseDateTicketColumn.setCellValueFactory(cellData -> cellData.getValue().purchaseDateProperty().asString());
        flightPriceTicketColumn.setCellValueFactory(cellData -> cellData.getValue().pricePropertyTicket().asObject().asString());
        selectTicketForDeletionText.textProperty().bindBidirectional(employeeController.ticketIdForDeletionProperty(), new NumberStringConverter());
        changeUserClientTicketText.textProperty().bindBidirectional(employeeController.userIdProperty());
        changeFlightNumberTicketText.textProperty().bindBidirectional(employeeController.flightIdProperty());
        changeTicketIdText.textProperty().bindBidirectional(employeeController.getTicketIdProperty());


        deleteTicketButton.setOnAction(event -> onDeleteTicketButtonClicked(event));
        addTicketButton.setOnAction(event -> onAddTicketButtonClicked(event));

        employeeController.createFlightsTable();
        employeeController.createTicketsTable();
        flightTableView.itemsProperty().bind(employeeController.getFlightsProperty());
        ticketTableView.itemsProperty().bind(employeeController.getTicketsProperty());
        update();
    }
    public void onSearchByFlightNumberClicked(ActionEvent actionEvent) {
       employeeController.searchFlightByNumber(searchByFlightNumberText.getText());
    }
    public void onSwitchToTravelerClicked(ActionEvent actionEvent) {
       employeeController.switchToTravelerView();
    }
    public void onSaveFlightDataAsFileClicked(ActionEvent actionEvent) {
        employeeController.saveFlightsAsData();
    }
    public void onDeleteFlightButtonClicked(ActionEvent actionEvent) {
       employeeController.deleteFlight(selectFlightForDeletionText.getText());
    }

    public void onAddFlightButtonClicked(ActionEvent actionEvent) {
        employeeController.createFlight();
    }

    public void onUpdateFlightButtonClicked(ActionEvent actionEvent) {
        System.out.println("Update button clicked");
        employeeController.updateFlight();
    }
    public void onExitButtonClicked(ActionEvent actionEvent) {
    }
    public void onUpdateTicketButtonClicked(ActionEvent actionEvent) {
        employeeController.updateTicket();
    }
    public void onAddTicketButtonClicked(ActionEvent actionEvent) {
        employeeController.createTicket();
    }
    public void onDeleteTicketButtonClicked(ActionEvent actionEvent) {
        employeeController.deleteTicket();
    }

    public void onViewStatisticsClicked(ActionEvent actionEvent) {
        ChartsView.show();
    }


    @Override
    public void update() {
            searchByFlightNumberText.setPromptText(LanguageManager.getInstance().getText("searchByFlightNumberText"));

        flightsControlLabel.setText(LanguageManager.getInstance().getText("flightsControlLabel"));
        departureTimeLabel.setText(LanguageManager.getInstance().getText("departureTimeLabel"));
        destinationAirportLabel.setText(LanguageManager.getInstance().getText("destinationAirportLabel"));
        departureAirportLabel.setText(LanguageManager.getInstance().getText("departureAirportLabel"));
        arrivalTimeLabel.setText(LanguageManager.getInstance().getText("arrivalTimeLabel"));
        flightNumberLabel.setText(LanguageManager.getInstance().getText("flightNumberLabel"));
        userIdLabel.setText(LanguageManager.getInstance().getText("userIdLabel"));
        flightIdLabel.setText(LanguageManager.getInstance().getText("flightIdLabel"));
        ticketsControlLabel.setText(LanguageManager.getInstance().getText("ticketsControlLabel"));
        priceLabel.setText(LanguageManager.getInstance().getText("priceLabel"));
        numberOfSeatsLabel.setText(LanguageManager.getInstance().getText("numberOfSeatsLabel"));
        ticketIdLabel.setText(LanguageManager.getInstance().getText("ticketIdLabel"));

        // Bind fields
        searchByFlightNumberText.setPromptText(LanguageManager.getInstance().getText("searchByFlightNumberText"));
        changeDepartureAirportText.setPromptText(LanguageManager.getInstance().getText("changeDepartureAirportText"));
        changeDestinationAirportText.setPromptText(LanguageManager.getInstance().getText("changeDestinationAirportText"));
        changeArrivalTimeText.setPromptText(LanguageManager.getInstance().getText("changeArrivalTimeText"));
        changeDepartureTimeText.setPromptText(LanguageManager.getInstance().getText("changeDepartureTimeText"));
        changeFlightNumberText.setPromptText(LanguageManager.getInstance().getText("changeFlightNumberText"));
        changeNoOfSeatsText.setPromptText(LanguageManager.getInstance().getText("changeNoOfSeatsText"));
        changePriceText.setPromptText(LanguageManager.getInstance().getText("changePriceText"));
        changeTicketIdText.setPromptText(LanguageManager.getInstance().getText("changeTicketIdText"));
        selectFlightForDeletionText.setPromptText(LanguageManager.getInstance().getText("selectFlightForDeletionText"));
        changeUserClientTicketText.setPromptText(LanguageManager.getInstance().getText("changeUserClientTicketText"));
        changeFlightNumberTicketText.setPromptText(LanguageManager.getInstance().getText("changeFlightNumberTicketText"));
        selectTicketForDeletionText.setPromptText(LanguageManager.getInstance().getText("selectTicketForDeletionText"));

        // Bind button text
        searchFlightButton.setText(LanguageManager.getInstance().getText("searchFlightButton"));
        saveFlightDataAsFile.setText(LanguageManager.getInstance().getText("saveFlightDataAsFile"));
        switchToTravelerButton.setText(LanguageManager.getInstance().getText("switchToTravelerButton"));
        deleteFlightButton.setText(LanguageManager.getInstance().getText("deleteFlightButton"));
        addFlightButton.setText(LanguageManager.getInstance().getText("addFlightButton"));
        updateFlightButton.setText(LanguageManager.getInstance().getText("updateFlightButton"));
        viewStatisticsButton.setText(LanguageManager.getInstance().getText("viewStatisticsButton"));
        addTicketButton.setText(LanguageManager.getInstance().getText("addTicketButton"));
        deleteTicketButton.setText(LanguageManager.getInstance().getText("deleteTicketButton"));
        updateTicketButton.setText(LanguageManager.getInstance().getText("updateTicketButton"));

        // Bind table column headers
        departureColumn.setText(LanguageManager.getInstance().getText("departureColumn"));
        destinationColumn.setText(LanguageManager.getInstance().getText("destinationColumn"));
        flightNumber.setText(LanguageManager.getInstance().getText("flightNumber"));
        departureTime.setText(LanguageManager.getInstance().getText("departureTime"));
        arrivalTime.setText(LanguageManager.getInstance().getText("arrivalTime"));
        priceFlight.setText(LanguageManager.getInstance().getText("priceFlight"));
        emptySeats.setText(LanguageManager.getInstance().getText("emptySeats"));
        ticketIdColumn.setText(LanguageManager.getInstance().getText("ticketIdColumn"));
        usernameTicketColumn.setText(LanguageManager.getInstance().getText("usernameTicketColumn"));
        flightNumberTicketColumn.setText(LanguageManager.getInstance().getText("flightNumberTicketColumn"));
        purchaseDateTicketColumn.setText(LanguageManager.getInstance().getText("purchaseDateTicketColumn"));
        flightPriceTicketColumn.setText(LanguageManager.getInstance().getText("flightPriceTicketColumn"));
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LanguageManager.getInstance().removeObserver(this);
    }
}
