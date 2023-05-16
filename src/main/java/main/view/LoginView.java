package main.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.controller.ControllerLogin;
import main.controller.utils.LanguageManager;
import main.model.Observer;


import java.io.IOException;
import java.util.Locale;

public class LoginView implements Observer {

    @FXML
    private Button button_french;
    @FXML
    private Button button_romanian;
    @FXML
    private Button button_english;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label titleOfApp;
    @FXML
    private TextField tf_username_login;
    @FXML
    private PasswordField tf_password_login;
    @FXML
    private Button button_login;
    @FXML
    private Button button_view_flights;
    private ControllerLogin loginController;
    private LanguageManager languageManager;
    public LoginView() {
        languageManager = new LanguageManager();
        languageManager = LanguageManager.getInstance();
        languageManager.addObserver(this);

        loginController = new ControllerLogin();
    }
    public void initialize() {
        tf_username_login.textProperty().bindBidirectional(loginController.usernameProperty());
        tf_password_login.textProperty().bindBidirectional(loginController.passwordProperty());

        update();
    }
    @FXML
    private void onLoginClicked() {
        loginController.loginMethod();
    }

    @FXML
    private void onViewFlightsClicked() {
        loginController.viewFlights();
    }

    public static void show() {
        FXMLLoader loader = new FXMLLoader(LoginView.class.getResource("/main/login-view.fxml"));
        HBox root;
        try {
            root = loader.load();
            LoginView loginView = loader.getController();
            loginView.initialize();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Airline Ticketing Application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onEnglishClicked(ActionEvent actionEvent) {
        languageManager.setLocale(Locale.ENGLISH);
    }

    public void onRomanianClicked(ActionEvent actionEvent) {
        languageManager.setLocale(new Locale("ro", "RO"));
    }

    public void onFrenchClicked(ActionEvent actionEvent) {
        languageManager.setLocale(Locale.FRENCH);
    }

    @Override
    public void update() {
        titleOfApp.setText(languageManager.getText("titleOfApp"));
        usernameLabel.setText(languageManager.getText("usernameLabel"));
        passwordLabel.setText(languageManager.getText("passwordLabel"));
        button_french.setText(languageManager.getText("button_french"));
        button_english.setText(languageManager.getText("button_english"));
        button_romanian.setText(languageManager.getText("button_romanian"));
        button_login.setText(languageManager.getText("onLoginClicked"));
        button_view_flights.setText(languageManager.getText("onViewFlightsClicked"));
    }
}