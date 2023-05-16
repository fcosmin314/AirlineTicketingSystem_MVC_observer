package main.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controller.ControllerAdmin;
import main.controller.utils.LanguageManager;
import main.model.User.UserModel;
import main.model.Observer;

import java.io.IOException;

public class AdminView implements Observer {
    public Label userIDlabel;
    public Label lastNameLabel;
    public Label userTypeLabel;
    public Label firstNameLabel;
    public Label passwordLabel;
    public Button viewEmployeeFlightsButton;
    public Label usernameLabel;
    @FXML
    private TextField userIdText;
    @FXML
    private TextField fistNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField userTypeText;
    @FXML
    private Button searchUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button addUserButton;
    @FXML
    private TableView<UserModel> usersTable;
    @FXML
    private TableColumn<UserModel, String> userIdColumn;
    @FXML
    private TableColumn<UserModel, String> firstNameColumn;
    @FXML
    private TableColumn<UserModel, String> lastNameColumn;
    @FXML
    private TableColumn<UserModel, String> usernameColumn;
    @FXML
    private TableColumn<UserModel, String> passwordColumn;
    @FXML
    private TableColumn<UserModel, String> userTypeColumn;


    private ControllerAdmin adminController;
    public AdminView(){

        adminController = new ControllerAdmin();
        LanguageManager.getInstance().addObserver(this);
    }

    public static void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TravelerView.class.getResource("/main/admin-view.fxml"));
            Parent root = fxmlLoader.load();

            ControllerAdmin adminController = new ControllerAdmin();
            AdminView adminView = fxmlLoader.getController();
            adminView.initialize();

            Stage travelerStage = new Stage();
            travelerStage.initModality(Modality.APPLICATION_MODAL);
            travelerStage.setTitle("Admin View");
            travelerStage.setScene(new Scene(root));
            travelerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initialize() {
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        userTypeColumn.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty());


        userIdText.textProperty().bindBidirectional(adminController.userIdPropertyProperty());
        fistNameText.textProperty().bindBidirectional(adminController.firstNameProperty());
        lastNameText.textProperty().bindBidirectional(adminController.lastNameProperty());
        usernameText.textProperty().bindBidirectional(adminController.usernameProperty());
        passwordText.textProperty().bindBidirectional(adminController.passwordProperty());
        userTypeText.textProperty().bindBidirectional(adminController.userTypeProperty());
        // usersTable.setItems(adminController.getUserViewModels());

        // Call command to create Users table
        deleteUserButton.setOnAction(event -> onDeleteUserButtonClicked(event));
        addUserButton.setOnAction(event -> onAddUserButtonClicked(event));
        updateUserButton.setOnAction(event -> onUpdateUserButtonClicked(event));


        usersTable.itemsProperty().bind(adminController.getUsersProperty());

        // Call the createUsersTable method when the view is initialized
        adminController.createUsersTable();
        update();

    }
    public void onSearchUserButtonClicked(ActionEvent actionEvent) {
        adminController.searchUser();
    }
    public void onDeleteUserButtonClicked(ActionEvent actionEvent) {
       adminController.deleteUser();
    }
    public void onUpdateUserButtonClicked(ActionEvent actionEvent) {
        adminController.updateUser();
    }
    public void onAddUserButtonClicked(ActionEvent actionEvent) {
        adminController.createUser();
    }
    public void onViewEmployeeFlightsButtonClicked(ActionEvent actionEvent) {
        adminController.switchToTravelerFromAdmin();
    }

    @Override
    public void update() {
        userIDlabel.setText(LanguageManager.getInstance().getText("userIDlabel.text"));
        lastNameLabel.setText(LanguageManager.getInstance().getText("lastNameLabel.text"));
        userTypeLabel.setText(LanguageManager.getInstance().getText("userTypeLabel.text"));
        firstNameLabel.setText(LanguageManager.getInstance().getText("firstNameLabel.text"));
        passwordLabel.setText(LanguageManager.getInstance().getText("passwordLabel.text"));
        usernameLabel.setText(LanguageManager.getInstance().getText("usernameLabel.text"));

        // Bind fields
        userIdText.setPromptText(LanguageManager.getInstance().getText("userIDText.promptText"));
        fistNameText.setPromptText(LanguageManager.getInstance().getText("fistNameText.promptText"));
        lastNameText.setPromptText(LanguageManager.getInstance().getText("lastNameText.promptText"));
        usernameText.setPromptText(LanguageManager.getInstance().getText("usernameText.promptText"));
        passwordText.setPromptText(LanguageManager.getInstance().getText("passwordText.promptText"));
        userTypeText.setPromptText(LanguageManager.getInstance().getText("userTypeText.promptText"));

        // Bind button text
        searchUserButton.setText(LanguageManager.getInstance().getText("searchUserButton.text"));
        deleteUserButton.setText(LanguageManager.getInstance().getText("deleteUserButton.text"));
        updateUserButton.setText(LanguageManager.getInstance().getText("updateUserButton.text"));
        addUserButton.setText(LanguageManager.getInstance().getText("addUserButton.text"));
        viewEmployeeFlightsButton.setText(LanguageManager.getInstance().getText("viewEmployeeFlightsButton.text"));

        // Bind table column headers
        userIdColumn.setText(LanguageManager.getInstance().getText("userIdColumn.text"));
        firstNameColumn.setText(LanguageManager.getInstance().getText("firstNameColumn.text"));
        lastNameColumn.setText(LanguageManager.getInstance().getText("lastNameColumn.text"));
        usernameColumn.setText(LanguageManager.getInstance().getText("usernameColumn.text"));
        passwordColumn.setText(LanguageManager.getInstance().getText("passwordColumn.text"));
        userTypeColumn.setText(LanguageManager.getInstance().getText("userTypeColumn.text"));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LanguageManager.getInstance().removeObserver(this);
    }
}
