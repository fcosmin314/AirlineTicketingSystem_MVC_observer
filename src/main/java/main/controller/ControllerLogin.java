package main.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.model.Observer;
import main.model.User.User;
import main.model.User.UserDAO;
import main.view.AdminView;
import main.view.EmployeeView;
import main.view.LoginView;
import main.view.TravelerView;

public class ControllerLogin implements Observer {
    private StringProperty username;
    private StringProperty password;

    private UserDAO userDAO;

    public ControllerLogin(){
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        userDAO = new UserDAO();
    }

    public void loginMethod(){
        String username = getUsername();
        String password = getPassword();

        User user = userDAO.findByUsername(username);
        if (user == null) {
            LoginView.showMessage("Username does not exist!");
        } else {
            if (password.equals(user.getPassword())) {
                String userType = user.getUserType();
                if ("administrator".equals(userType)) {
                    AdminView.show();
                    LoginView.showMessage(userType);
                } else if ("employee".equals(userType)) {
                    EmployeeView.show();
                    LoginView.showMessage(userType);
                } else if ("traveler".equals(userType)) {
                    TravelerView.show();
                    LoginView.showMessage(userType);
                }
            } else {
                LoginView.showMessage("Incorrect password!");
            }
        }
    }

    public void viewFlights() {
        TravelerView.show();
    }
    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public void update() {

    }
}
