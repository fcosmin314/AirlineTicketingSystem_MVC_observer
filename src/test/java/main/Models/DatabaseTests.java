package main.Models;

import main.model.Connection.DBhelper;
import main.model.Flight.Flight;
import main.model.Flight.FlightDAO;
import main.model.Ticket.TicketDAO;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTests {

    private FlightDAO flightDAO;
    private TicketDAO ticketDAO;

    public DatabaseTests() {
        flightDAO = new FlightDAO();
    }
    private static Connection connection;

    @BeforeAll
    static void setUp() {
        connection = DBhelper.getConnection();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }
    @Test
    public void testDatabaseConnection() {
        try {
            Connection connection = DBhelper.getConnection();
            if (connection != null) {
                System.out.println("Database connection is successful!");
                connection.close();
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    @Test
    void testDropTables() {
        try (Connection connection = DBhelper.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS airline_ticketing_for_tests.tickets");
            statement.execute("DROP TABLE IF EXISTS airline_ticketing_for_tests.flights");
            statement.execute("DROP TABLE IF EXISTS airline_ticketing_for_tests.airports");
            statement.execute("DROP TABLE IF EXISTS airline_ticketing_for_tests.users");


        } catch (SQLException e) {
            fail("Error occurred while dropping tables: " + e.getMessage());
        }
    }
    @BeforeEach
    void testCreateTables() {
        assertTrue(DBhelper.createTables(), "Tables should be created successfully");

        // Check if the tables are created successfully
        try (Connection connection = DBhelper.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SHOW TABLES IN airline_ticketing_for_tests");
            Set<String> tableNames = new HashSet<>();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }

            assertTrue(tableNames.contains("users"), "Table 'users' should be created");
            assertTrue(tableNames.contains("airports"), "Table 'airports' should be created");
            assertTrue(tableNames.contains("flights"), "Table 'flights' should be created");
            assertTrue(tableNames.contains("tickets"), "Table 'tickets' should be created");

        } catch (SQLException e) {
            fail("Error occurred while checking if the tables are created: " + e.getMessage());
        }
    }
    @Test
    void testUsersCRUD() {
        // Insert a user
        String insertUserQuery = "INSERT INTO users (first_name, last_name, username, password, user_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, "John");
            preparedStatement.setString(2, "Doe");
            preparedStatement.setString(3, "johndoe");
            preparedStatement.setString(4, "password123");
            preparedStatement.setString(5, "traveler");
            int affectedRows = preparedStatement.executeUpdate();
            assertEquals(1, affectedRows, "Inserting a user should return 1 affected row");

            // Get the generated user id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            assertTrue(generatedKeys.next(), "A user id should be generated");
            int userId = generatedKeys.getInt(1);

            // Read the user
            String selectUserQuery = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement selectPreparedStatement = connection.prepareStatement(selectUserQuery)) {
                selectPreparedStatement.setInt(1, userId);
                ResultSet resultSet = selectPreparedStatement.executeQuery();
                assertTrue(resultSet.next(), "A user should be found with the generated id");
                assertEquals("John", resultSet.getString("first_name"), "First name should match");
                assertEquals("Doe", resultSet.getString("last_name"), "Last name should match");
                assertEquals("johndoe", resultSet.getString("username"), "Username should match");
                assertEquals("password123", resultSet.getString("password"), "Password should match");
                assertEquals("traveler", resultSet.getString("user_type"), "User type should match");

                // Update the user
                String updateUserQuery = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, user_type = ? WHERE id = ?";
                try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateUserQuery)) {
                    updatePreparedStatement.setString(1, "Jane");
                    updatePreparedStatement.setString(2, "Doe");
                    updatePreparedStatement.setString(3, "janedoe");
                    updatePreparedStatement.setString(4, "password456");
                    updatePreparedStatement.setString(5, "employee");
                    updatePreparedStatement.setInt(6, userId);
                    int updateAffectedRows = updatePreparedStatement.executeUpdate();
                    assertEquals(1, updateAffectedRows, "Updating a user should return 1 affected row");

                    // Read the updated user
                    ResultSet updatedResultSet = selectPreparedStatement.executeQuery();
                    assertTrue(updatedResultSet.next(), "An updated user should be found with the generated id");
                    assertEquals("Jane", updatedResultSet.getString("first_name"), "Updated first name should match");
                    assertEquals("Doe", updatedResultSet.getString("last_name"), "Updated last name should match");
                    assertEquals("janedoe", updatedResultSet.getString("username"), "Updated username should match");
                    assertEquals("password456", updatedResultSet.getString("password"), "Updated password should match");
                    assertEquals("employee", updatedResultSet.getString("user_type"), "Updated user type should match");
                }

                // Delete the user
                String deleteUserQuery = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteUserQuery)) {
                    deletePreparedStatement.setInt(1, userId);
                    int deleteAffectedRows = deletePreparedStatement.executeUpdate();
                    assertEquals(1, deleteAffectedRows, "Deleting a user should return 1 affected row");

                    // Check if the user is deleted
                    ResultSet deletedResultSet = selectPreparedStatement.executeQuery();
                    assertFalse(deletedResultSet.next(), "No user should be found with the generated id after deletion");
                } catch (SQLException e) {
                    fail("Error occurred while performing CRUD operations on the users table: " + e.getMessage());
                }
            } catch (SQLException e) {
                fail("Error occurred while inserting a user: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFlightsCRUD() {
        // Creating a new flight
        Flight newFlight = new Flight("AA123", "JFK", "LAX", LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusHours(10), 500.0, 200);
        boolean flightCreated = flightDAO.createFlight(newFlight);
        System.out.println("Flight created: " + flightCreated);

        // Retrieve flight by flight number
        String flightId = flightDAO.getFlightIdByFlightNumber("AA123");
        if (flightId != null) {
            Flight flight = flightDAO.getFlightById(Integer.parseInt(flightId));
            System.out.println("Flight number: " + flight.getFlightNumber());

            // Update flight
            flight.setPrice(550.0);
            boolean flightUpdated = flightDAO.updateFlight(flight);
            System.out.println("Flight updated: " + flightUpdated);

            // Delete flight
            boolean flightDeleted = flightDAO.deleteFlight(flight.getFlightNumber());
            System.out.println("Flight deleted: " + flightDeleted);
        }
    }

    //does not work bcs of the createTicket in TicketService. Look at it!
//    @Test
//    public void testTicketCRUD() {
//        // Creating a new ticket
//        LocalDateTime now = LocalDateTime.now();
//        Ticket newTicket = new Ticket(1, 1, 1, now);
//        boolean ticketCreated = ticketService.createTicket(newTicket);
//        System.out.println("Ticket created: " + ticketCreated);
//
//        // Retrieve ticket by ID
//        List<Ticket> tickets = ticketService.getAllTickets();
//        int ticketId = tickets.get(tickets.size() - 1).getId();
//        Ticket ticket = ticketService.getTicketById(ticketId);
//        System.out.println("Ticket ID: " + ticket.getId());
//
//        // Update ticket
//        ticket.setPrice(550.0);
//        boolean ticketUpdated = ticketService.updateTicket(ticket);
//        System.out.println("Ticket updated: " + ticketUpdated);
//
//        // Delete ticket
//        boolean ticketDeleted = ticketService.deleteTicketById(ticket.getId());
//        System.out.println("Ticket deleted: " + ticketDeleted);
//    }

}
