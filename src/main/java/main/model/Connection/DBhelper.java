package main.model.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBhelper {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/airline_ticketing?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //for tests>>
    //private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/airline_ticketing_for_tests?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "mysql";
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }


    public static boolean createTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE SCHEMA IF NOT EXISTS airline_ticketing_for_tests DEFAULT CHARACTER SET utf8mb4");
            statement.execute("USE airline_ticketing_for_tests");
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, first_name VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, user_type ENUM('traveler', 'employee', 'administrator') NOT NULL)");
            statement.execute("CREATE TABLE IF NOT EXISTS airports (id INT AUTO_INCREMENT PRIMARY KEY, code VARCHAR(10) NOT NULL UNIQUE, name VARCHAR(255) NOT NULL, city VARCHAR(255) NOT NULL, country VARCHAR(255) NOT NULL)");
            statement.execute("CREATE TABLE IF NOT EXISTS flights (id INT AUTO_INCREMENT PRIMARY KEY, flight_number VARCHAR(10) NOT NULL UNIQUE, departure_airport_id INT NOT NULL, destination_airport_id INT NOT NULL, departure_time DATETIME NOT NULL, arrival_time DATETIME NOT NULL, price DECIMAL(10, 2) NOT NULL, number_of_seats INT NOT NULL, FOREIGN KEY (departure_airport_id) REFERENCES airports(id), FOREIGN KEY (destination_airport_id) REFERENCES airports(id))");
            statement.execute("CREATE TABLE IF NOT EXISTS tickets (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, flight_id INT NOT NULL, purchase_date DATETIME NOT NULL, price DECIMAL(10, 2) NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (flight_id) REFERENCES flights(id))");

            return true; // return true if tables are created success
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }
}
