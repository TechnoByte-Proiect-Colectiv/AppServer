package ProiectColectiv.Domain;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Entity<String> {
    private String password;
    private boolean isAdmin;
    private  String authToken;
    private LocalDateTime lastLogin;
    private  String Adress;
    private LocalDate dateCreated;
}
