package ProiectColectiv.Domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Entity<String> {
    private String password;
    private boolean isAdmin;
    private String authToken;
    private String firstName;
    private String lastName;
    private LocalDateTime lastLogin;
    private String address;
    private LocalDate dateCreated;

    public User(String firstName, String lastName, String email, String password, boolean isAdmin, String authToken, LocalDateTime lastLogin, String address, LocalDate dateCreated) {
        this.firstName = firstName;
        this.lastName = lastName;
        setId(email);
        this.password = password;
        this.isAdmin = isAdmin;
        this.authToken = authToken;
        this.lastLogin = lastLogin;
        this.address = address;
        this.dateCreated = dateCreated;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return getId();
    }

    public void setEmail(String email) {
        setId(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getAddress() {
        return address;
    }

    public void setAdress(String address) {
        this.address = address;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}