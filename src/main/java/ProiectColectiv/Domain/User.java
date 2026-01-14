package ProiectColectiv.Domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User extends Entity<String> {
    private String password;
    private boolean isAdmin;
    private String authToken;
    private String firstName;
    private String lastName;
    private LocalDateTime lastLogin;
    private List<Address> addresses;
    private LocalDate dateCreated;
    private String phoneNumber;

    public User(String firstName, String lastName, String email, String password, boolean isAdmin, String authToken, LocalDateTime lastLogin, LocalDate dateCreated, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        setId(email);
        this.password = password;
        this.isAdmin = isAdmin;
        this.authToken = authToken;
        this.lastLogin = lastLogin;
        this.dateCreated = dateCreated;
        this.phoneNumber = phoneNumber;
        this.addresses = new ArrayList<>();
    }

    public User() {
        this.addresses = new ArrayList<>();
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPhoneNumber() { return  phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}