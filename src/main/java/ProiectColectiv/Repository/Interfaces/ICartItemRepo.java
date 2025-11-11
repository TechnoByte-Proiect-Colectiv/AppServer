package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;

public interface ICartItemRepo extends Repository<CompositeKey<String,Integer>, CartItem>{
    Iterable<CartItem> findAllForUser(String userID);
    void clearCartForUser(String userID);
}