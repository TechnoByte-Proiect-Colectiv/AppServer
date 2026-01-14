package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.DatabaseRepo.UserRepo;

public interface IUserRepo extends Repository<String, User> {
    Iterable<User> getAllUsers();
    void updatePassword(User user);
    User findByToken(String token);
}
