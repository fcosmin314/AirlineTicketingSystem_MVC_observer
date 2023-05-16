package main.model.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.model.AbstractObserver;

public class UserModel extends AbstractObserver {

    private User user;
    private StringProperty id;
    private StringProperty username;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty userType;

    public UserModel(User user){
        this.user = user;
        id = new SimpleStringProperty(user.getId());
        username = new SimpleStringProperty(user.getUsername());
        password = new SimpleStringProperty(user.getPassword());
        firstName = new SimpleStringProperty(user.getFirstName());
        lastName = new SimpleStringProperty(user.getLastName());
        userType = new SimpleStringProperty(user.getUserType());
        notifyObservers();
    }

    public StringProperty userIdProperty(){return id;}
    public StringProperty usernameProperty(){return username;}
    public StringProperty passwordProperty(){return password;}
    public StringProperty firstNameProperty(){return firstName;}
    public StringProperty lastNameProperty(){return lastName;}
    public StringProperty userTypeProperty(){return userType;}
}
