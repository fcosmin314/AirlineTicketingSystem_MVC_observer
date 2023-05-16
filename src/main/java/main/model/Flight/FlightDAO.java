package main.model.Flight;

import main.model.Connection.DBhelper;
import main.model.Ticket.TicketDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightDAO {
    private Flight createFlightFromResultSet(ResultSet resultSet) throws SQLException {
        Flight flight = new Flight();
        flight.setFlightNumber(resultSet.getString("flight_number"));
        flight.setDepartureAirport(resultSet.getString("departure_airport"));
        flight.setDestinationAirport(resultSet.getString("destination_airport"));
        flight.setDepartureTime(resultSet.getTimestamp("departure_time").toLocalDateTime());
        flight.setArrivalTime(resultSet.getTimestamp("arrival_time").toLocalDateTime());
        flight.setPrice(resultSet.getInt("price"));
        flight.setNumberOfSeats(resultSet.getInt("number_of_seats"));

        return flight;
    }
    public List<Flight> getFlights() {
        List<Flight> flights = new ArrayList<>();

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number, a1.name AS departure_airport, a2.name AS destination_airport, " +
                    "f.departure_time, f.arrival_time, f.price, f.number_of_seats " +
                    "FROM flights f " +
                    "JOIN airports a1 ON f.departure_airport_id = a1.id " +
                    "JOIN airports a2 ON f.destination_airport_id = a2.id " +
                    "ORDER BY a1.name, a2.name";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                flights.add(createFlightFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    public Flight getFlightById(int flightId) {
        Flight flight = null;

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT * FROM flights WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, flightId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                flight = createFlightFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flight;
    }
    public List<Flight> getFlightsBetweenAirports(String departureAirport, String destinationAirport) {
        List<Flight> flights = new ArrayList<>();

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number, a1.name AS departure_airport, a2.name AS destination_airport, " +
                    "f.departure_time, f.arrival_time, f.price, f.number_of_seats " +
                    "FROM flights f " +
                    "JOIN airports a1 ON f.departure_airport_id = a1.id " +
                    "JOIN airports a2 ON f.destination_airport_id = a2.id " +
                    "WHERE (a1.name = ? AND a2.name = ?) OR (a1.name = ? AND a2.name = ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, departureAirport);
            preparedStatement.setString(2, destinationAirport);
            preparedStatement.setString(3, destinationAirport);
            preparedStatement.setString(4, departureAirport);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Flight flight = new Flight();
                flight.setFlightNumber(resultSet.getString("flight_number"));
                flight.setDepartureAirport(resultSet.getString("departure_airport"));
                flight.setDestinationAirport(resultSet.getString("destination_airport"));
                flight.setDepartureTime(resultSet.getTimestamp("departure_time").toLocalDateTime());
                flight.setArrivalTime(resultSet.getTimestamp("arrival_time").toLocalDateTime());
                flight.setPrice(resultSet.getInt("price"));
                flight.setNumberOfSeats(resultSet.getInt("number_of_seats"));

                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }
    public boolean createFlight(Flight flight) {
        try (Connection connection = DBhelper.getConnection()) {
            String query = "INSERT INTO flights (flight_number, departure_airport_id, destination_airport_id, departure_time, arrival_time, price, number_of_seats) " +
                    "SELECT ?, a1.id, a2.id, ?, ?, ?, ? " +
                    "FROM airports a1, airports a2 " +
                    "WHERE a1.name = ? AND a2.name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, flight.getFlightNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(flight.getArrivalTime()));
            preparedStatement.setDouble(4, flight.getPrice());
            preparedStatement.setInt(5, flight.getNumberOfSeats());
            preparedStatement.setString(6, flight.getDepartureAirport());
            preparedStatement.setString(7, flight.getDestinationAirport());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getFlightPrice(int flightId) {
        double price = 0.0;
        String query = "SELECT price FROM flights WHERE id = ?";
        try (Connection connection = DBhelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, flightId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }
    public boolean updateFlight(Flight flight) {
        System.out.println("Updating flight in the database");
        try (Connection connection = DBhelper.getConnection()) {
            String query = "UPDATE flights " +
                    "SET departure_airport_id = (SELECT id FROM airports WHERE name = ?), " +
                    "destination_airport_id = (SELECT id FROM airports WHERE name = ?), " +
                    "departure_time = ?, arrival_time = ?, price = ?, number_of_seats = ? " +
                    "WHERE flight_number = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, flight.getDepartureAirport());
            preparedStatement.setString(2, flight.getDestinationAirport());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(flight.getDepartureTime()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalTime()));
            preparedStatement.setDouble(5, flight.getPrice());
            preparedStatement.setInt(6, flight.getNumberOfSeats());
            preparedStatement.setString(7, flight.getFlightNumber());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFlightIdByFlightNumber(String flightNumber) {
        String sql = "SELECT id FROM flights WHERE flight_number = ?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flightNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public boolean deleteFlight(String flightNumber) {
        String flightId = getFlightIdByFlightNumber(flightNumber);

        TicketDAO ticketDAO = new TicketDAO();
        boolean ticketsDeleted = ticketDAO.deleteTicketsByFlightId(flightId);

        if (ticketsDeleted) {
            String sql = "DELETE FROM flights WHERE id = ?";
            try (Connection conn = DBhelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, flightId);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else {
            System.out.println("Failed to delete associated tickets.");
            return false;
        }
    }

    public Map<String, Integer> getNumberOfFlightsByAirport() {
        Map<String, Integer> flightsByAirport = new HashMap<>();

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT a1.name AS departure_airport, COUNT(*) as flight_count " +
                    "FROM flights f " +
                    "JOIN airports a1 ON f.departure_airport_id = a1.id " +
                    "GROUP BY a1.name";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String airport = resultSet.getString("departure_airport");
                Integer count = resultSet.getInt("flight_count");
                flightsByAirport.put(airport, count);
                System.out.println("Airport: " + airport + ", Count: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightsByAirport;
    }

    public Map<String, Double> getAveragePricePerFlight() {
        Map<String, Double> averagePricePerFlight = new HashMap<>();

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number AS flight, AVG(f.price) as average_price " +
                    "FROM flights f " +
                    "GROUP BY f.flight_number";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String flight = resultSet.getString("flight");
                double averagePrice = resultSet.getDouble("average_price");
                averagePricePerFlight.put(flight, averagePrice);
                System.out.println("Flight: " + flight + ", Average Price: " + averagePrice);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return averagePricePerFlight;
    }

    public Map<String, Integer> getTotalSeatsPerFlight() {
        Map<String, Integer> totalSeatsPerFlight = new HashMap<>();

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number AS flight, SUM(f.number_of_seats) as total_seats " +
                    "FROM flights f " +
                    "GROUP BY f.flight_number";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String flight = resultSet.getString("flight");
                int totalSeats = resultSet.getInt("total_seats");
                totalSeatsPerFlight.put(flight, totalSeats);
                System.out.println("Flight: " + flight + ", Total Seats: " + totalSeats);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalSeatsPerFlight;
    }
    public void saveFlightsToFile(String format) {
        switch (format) {
            case "csv":
                saveFlightsToCsv();
                break;
            case "json":
                saveFlightsToJson();
                break;
            case "xml":
                saveFlightsToXml();
                break;
            case "txt":
                saveFlightsToTxt();
                break;
            default:
                System.out.println("Invalid format");
                break;
        }
    }
    private void saveFlightsToCsv() {
        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT * FROM flights ORDER BY id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            FileWriter fileWriter = new FileWriter("flights.csv");
            fileWriter.write("id,flight_number,departure_airport_id,destination_airport_id,departure_time,arrival_time,price,number_of_seats\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String flightNumber = resultSet.getString("flight_number");
                int departureAirportId = resultSet.getInt("departure_airport_id");
                int destinationAirportId = resultSet.getInt("destination_airport_id");
                String departureTime = resultSet.getString("departure_time");
                String arrivalTime = resultSet.getString("arrival_time");
                double price = resultSet.getDouble("price");
                int numberOfSeats = resultSet.getInt("number_of_seats");

                fileWriter.write(String.format("%d,%s,%d,%d,%s,%s,%.2f,%d\n", id, flightNumber, departureAirportId, destinationAirportId, departureTime, arrivalTime, price, numberOfSeats));
            }

            fileWriter.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void saveFlightsToJson() {
        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number, a1.name AS departure_airport, a2.name AS destination_airport, " +
                    "f.departure_time, f.arrival_time, f.price, f.number_of_seats " +
                    "FROM flights f " +
                    "JOIN airports a1 ON f.departure_airport_id = a1.id " +
                    "JOIN airports a2 ON f.destination_airport_id = a2.id " +
                    "ORDER BY a1.name, a2.name";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("flight_number", resultSet.getString("flight_number"));
                record.put("departure_airport", resultSet.getString("departure_airport"));
                record.put("destination_airport", resultSet.getString("destination_airport"));
                record.put("departure_time", resultSet.getTimestamp("departure_time").toString());
                record.put("arrival_time", resultSet.getTimestamp("arrival_time").toString());
                record.put("price", resultSet.getInt("price"));
                record.put("number_of_seats", resultSet.getInt("number_of_seats"));

                array.add(record);
            }

            jsonObject.put("flights_data", array);

            try {
                FileWriter file = new FileWriter("flights_output.json");
                file.write(jsonObject.toJSONString());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("JSON file created......");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void saveFlightsToXml() {
        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT f.flight_number, a1.name AS departure_airport, a2.name AS destination_airport, " +
                    "f.departure_time, f.arrival_time, f.price, f.number_of_seats " +
                    "FROM flights f " +
                    "JOIN airports a1 ON f.departure_airport_id = a1.id " +
                    "JOIN airports a2 ON f.destination_airport_id = a2.id " +
                    "ORDER BY a1.name, a2.name";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Document xmlDoc = buildFlightXML(resultSet);
            File outputFile = new File("flights_output.xml");
            printDOM(xmlDoc, outputFile);

            System.out.println("XML file created......");
        } catch (SQLException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private Document buildFlightXML(ResultSet resultSet) throws SQLException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error creating document builder", e);
        }
        Document xmlDoc = dBuilder.newDocument();

        Element rootElement = xmlDoc.createElement("FlightTable");
        xmlDoc.appendChild(rootElement);

        while (resultSet.next()) {
            Element flight = xmlDoc.createElement("flight");
            flight.setAttribute("flight_number", resultSet.getString("flight_number"));

            Element departureAirport = xmlDoc.createElement("departure_airport");
            Element destinationAirport = xmlDoc.createElement("destination_airport");
            Element departureTime = xmlDoc.createElement("departure_time");
            Element arrivalTime = xmlDoc.createElement("arrival_time");
            Element price = xmlDoc.createElement("price");
            Element numberOfSeats = xmlDoc.createElement("number_of_seats");

            departureAirport.appendChild(xmlDoc.createTextNode(resultSet.getString("departure_airport")));
            destinationAirport.appendChild(xmlDoc.createTextNode(resultSet.getString("destination_airport")));
            departureTime.appendChild(xmlDoc.createTextNode(resultSet.getTimestamp("departure_time").toString()));
            arrivalTime.appendChild(xmlDoc.createTextNode(resultSet.getTimestamp("arrival_time").toString()));
            price.appendChild(xmlDoc.createTextNode(String.valueOf(resultSet.getInt("price"))));
            numberOfSeats.appendChild(xmlDoc.createTextNode(String.valueOf(resultSet.getInt("number_of_seats"))));

            flight.appendChild(departureAirport);
            flight.appendChild(destinationAirport);
            flight.appendChild(departureTime);
            flight.appendChild(arrivalTime);
            flight.appendChild(price);
            flight.appendChild(numberOfSeats);

            rootElement.appendChild(flight);
        }
        return xmlDoc;
    }
    private static void printDOM(Document xmlDoc, File outputFile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(xmlDoc);
        StreamResult result = new StreamResult(outputFile);

        transformer.transform(source, result);
    }
    private void saveFlightsToTxt() {
        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT * FROM flights ORDER BY id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            FileWriter fileWriter = new FileWriter("flights.txt");
            fileWriter.write("ID\tFlight Number\tDeparture Airport ID\tDestination Airport ID\tDeparture Time\tArrival Time\tPrice\tNumber of Seats\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String flightNumber = resultSet.getString("flight_number");
                int departureAirportId = resultSet.getInt("departure_airport_id");
                int destinationAirportId = resultSet.getInt("destination_airport_id");
                String departureTime = resultSet.getString("departure_time");
                String arrivalTime = resultSet.getString("arrival_time");
                double price = resultSet.getDouble("price");
                int numberOfSeats = resultSet.getInt("number_of_seats");

                fileWriter.write(String.format("%d\t%s\t%d\t%d\t%s\t%s\t%.2f\t%d\n", id, flightNumber, departureAirportId, destinationAirportId, departureTime, arrivalTime, price, numberOfSeats));
            }

            fileWriter.close();
        } catch (SQLException |IOException e) {
            e.printStackTrace();
        }
    }
}