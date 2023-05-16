package main.model.User;

import main.model.Connection.DBhelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DBhelper.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userType = resultSet.getString("user_type");

                users.add(new User(id, firstName, lastName, username, password, userType));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findByUsername(String username) {
        User user = null;

        try (Connection connection = DBhelper.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                String userType = resultSet.getString("user_type");

                user = new User(id, firstName, lastName, username, password, userType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    public boolean deleteUser(String userId) {
        if(userId != "") {
            // First delete the tickets associated with the user
            String ticketQuery = "DELETE FROM tickets WHERE user_id = ?";
            // Then delete the user
            String userQuery = "DELETE FROM users WHERE id = ?";

            try (Connection connection = DBhelper.getConnection()) {
                // Turn off auto-commit
                connection.setAutoCommit(false);

                // Delete tickets
                PreparedStatement ticketStatement = connection.prepareStatement(ticketQuery);
                ticketStatement.setString(1, userId);
                ticketStatement.executeUpdate();

                // Delete user
                PreparedStatement userStatement = connection.prepareStatement(userQuery);
                userStatement.setString(1, userId);
                userStatement.executeUpdate();

                // Commit the transaction
                connection.commit();

                return true;
            } catch (SQLException e) {
                try (Connection connection = DBhelper.getConnection()){
                    // If something went wrong, roll back the changes
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println(e.getMessage());
                return false;
            } finally {
                try (Connection connection = DBhelper.getConnection()){
                    // Turn auto-commit back on
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }else{
            System.out.println("failed to delete a user :(");
            return false;
        }
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (id, first_name, last_name, username, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBhelper.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(user.getId()));
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getUserType());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(User user) {
        String query = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, user_type = ? WHERE id = ?";

        try (Connection connection = DBhelper.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getUserType());
            preparedStatement.setInt(6, Integer.parseInt(user.getId()));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
