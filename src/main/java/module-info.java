module main.tema3_airlineticketing {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires json.simple;


    opens main to javafx.fxml;
    exports main;
    opens main.view to javafx.fxml;
    exports main.view;
}