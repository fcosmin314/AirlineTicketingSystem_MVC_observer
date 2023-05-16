package main.model.Ticket;

import main.model.Connection.DBhelper;
import main.model.Flight.Flight;
import main.model.User.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class TicketDAO {

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBhelper.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT t.id, t.user_id, t.flight_id, t.purchase_date, " +
                    "u.username, " +
                    "f.flight_number, f.price " +
                    "FROM tickets t " +
                    "JOIN users u ON t.user_id = u.id " +
                    "JOIN flights f ON t.flight_id = f.id";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int flightId = resultSet.getInt("flight_id");
                LocalDateTime purchaseDate = resultSet.getTimestamp("purchase_date").toLocalDateTime();

                String username = resultSet.getString("username");
                User user = new User(userId, username);

                String flightNumber = resultSet.getString("flight_number");
                double price = resultSet.getDouble("price");
                Flight flight = new Flight(flightNumber, price);

                Ticket ticket = new Ticket(id, userId, flightId, purchaseDate, user, flight);
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }
    public boolean deleteTicketsByFlightId(String flightId) {
        String sql = "DELETE FROM tickets WHERE flight_id = ?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flightId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean deleteTicketById(int ticketId) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean createTicket(Ticket ticket) {
        String query = "INSERT INTO tickets (user_id, flight_id, purchase_date, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBhelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, ticket.getUserId());
            preparedStatement.setInt(2, ticket.getFlightId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(ticket.getPurchaseDate()));
            Flight flight = new Flight();
            preparedStatement.setDouble(4, flight.getPrice());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//possible solution for tests? nope
//    public boolean createTicket(Ticket ticket) {
//        String query = "INSERT INTO tickets (user_id, flight_id, purchase_date, price) VALUES (?, ?, ?, ?)";
//
//        try (Connection connection = DBhelper.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, ticket.getUserId());
//            preparedStatement.setInt(2, ticket.getFlightId());
//            preparedStatement.setTimestamp(3, Timestamp.valueOf(ticket.getPurchaseDate()));
//            preparedStatement.setDouble(4, ticket.getFlight().getPrice());
//
//            int affectedRows = preparedStatement.executeUpdate();
//            return affectedRows > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public Ticket getTicketById(int id) {
        Ticket ticket = null;
        try (Connection connection = DBhelper.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM tickets WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int flightId = resultSet.getInt("flight_id");
                LocalDateTime purchaseDate = resultSet.getTimestamp("purchase_date").toLocalDateTime();

                String username = resultSet.getString("username");
                User user = new User(userId, username);

                String flightNumber = resultSet.getString("flight_number");
                double price = resultSet.getDouble("price");
                Flight flight = new Flight(flightNumber, price);

                ticket = new Ticket(id, userId, flightId, purchaseDate, user, flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        String query = "UPDATE tickets SET user_id = ?, flight_id = ?, purchase_date = ?, price = ? WHERE id = ?";

        try (Connection connection = DBhelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, ticket.getUserId());
            preparedStatement.setInt(2, ticket.getFlightId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(ticket.getPurchaseDate()));
            preparedStatement.setDouble(4, ticket.getPrice());
            preparedStatement.setInt(5, ticket.getId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}