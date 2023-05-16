package main.model.User;

public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userType;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(){
        this.username = getUsername();
    }

    public User(int id, String firstName, String lastName, String username, String password, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    public User(int userId, String username) {
        this.id = userId;
        this.username = username;
    }

    public String getId() {
        return String.valueOf(id);
    }
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserType() {
        return userType;
    }
}
