package ProiectColectiv.Domain;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Entity<String> {
    private String password;
    private boolean isAdmin;
    private  String authToken;
    private LocalDateTime lastLogin;
    private  String adress;
    private LocalDate dateCreated;

    public User(String password, boolean isAdmin, String authToken, LocalDateTime lastLogin, String Adress, LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        this.adress = Adress;
        this.lastLogin = lastLogin;
        this.authToken = authToken;
        this.isAdmin = isAdmin;
        this.password = password;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String Adress) {
        adress = Adress;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}
