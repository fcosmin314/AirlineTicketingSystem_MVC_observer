package main.controller;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Observer;
import main.model.User.User;
import main.model.User.UserDAO;
import main.model.User.UserModel;
import main.view.TravelerView;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerAdmin implements Observer {
    private StringProperty userIdProperty;

    private StringProperty usernameProperty;
    private StringProperty passwordProperty;
    private StringProperty firstNameProperty;

    private StringProperty lastNameProperty;
    private StringProperty userTypeProperty;
    private ListProperty<UserModel> users;
    private UserDAO userDAO;

    private ObservableList<ControllerAdmin> filteredUsers = FXCollections.observableArrayList();

    public ControllerAdmin() {

        userDAO = new UserDAO();
        users = new SimpleListProperty<>(FXCollections.observableArrayList());
        userIdProperty = new SimpleStringProperty();
        usernameProperty = new SimpleStringProperty();
        passwordProperty = new SimpleStringProperty();
        firstNameProperty = new SimpleStringProperty();
        lastNameProperty = new SimpleStringProperty();
        userTypeProperty = new SimpleStringProperty();

    }


    public void fetchUsers(){
        List<User> usersList = userDAO.getUsers();
        List<UserModel> UProperties = usersList.stream().map(UserModel::new).collect(Collectors.toList());
        users.setAll(UProperties);
        //observer notify
        //notifyObservers();
    }


    public void createUsersTable() {
        List<User> userList = userDAO.getUsers();
        List<UserModel> userModelList = userList.stream().map(UserModel::new).collect(Collectors.toList());
        users.setAll(userModelList);
    }

    public void searchUser() {
        String userIdToSearch = getIdAsString();
        List<UserModel> filteredUsers = getUsersProperty().stream()
                .filter(userModel -> userModel.userIdProperty().get().equalsIgnoreCase(userIdToSearch))
                .collect(Collectors.toList());
        users.setAll(filteredUsers);
    }

    public void switchToTravelerFromAdmin() {
        TravelerView.show();
    }

    public void deleteUser() {
        String userId = userIdPropertyProperty().get();
        if (userId != null && !userId.isEmpty()) {
            boolean success = userDAO.deleteUser(userId);
            if (success) {
                fetchUsers();
            }
        }
    }
    public void updateUser() {
        User user = new User();
        user.setId(userIdPropertyProperty().get());
        user.setUsername(usernameProperty().get());
        user.setPassword(passwordProperty().get());
        user.setFirstName(firstNameProperty().get());
        user.setLastName(lastNameProperty().get());
        user.setUserType(userTypeProperty().get());
        boolean success = userDAO.updateUser(user);
        if (success) {
            fetchUsers();
        }
    }

    public void createUser() {
        User user = new User();
        user.setId(userIdPropertyProperty().get());
        user.setUsername(usernameProperty().get());
        user.setPassword(passwordProperty().get());
        user.setFirstName(firstNameProperty().get());
        user.setLastName(lastNameProperty().get());
        user.setUserType(userTypeProperty().get());
        boolean success = userDAO.createUser(user);
        if (success) {
            fetchUsers();
        }
    }
    private ObjectProperty<ControllerAdmin> filteredUser = new SimpleObjectProperty<>();

    public ListProperty<UserModel> getUsersProperty(){ return users;}

    public UserDAO getUserService(){return userDAO;}


    public String getIdAsString(){
        return userIdProperty.get();
    }
    public StringProperty userIdPropertyProperty() {
        return userIdProperty;
    }

    public StringProperty usernameProperty(){
        return usernameProperty;
    }

    public StringProperty passwordProperty(){
        return passwordProperty;
    }

    public StringProperty firstNameProperty(){
        return firstNameProperty;
    }

    public StringProperty lastNameProperty(){
        return lastNameProperty;
    }

    public StringProperty userTypeProperty(){
        return userTypeProperty;
    }

    @Override
    public void update() {

    }
}