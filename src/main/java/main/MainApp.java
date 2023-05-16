package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.view.LoginView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // testDatabaseConnection();
        LoginView.show();
    }

    public static void main(String[] args) {
        launch();
    }
}