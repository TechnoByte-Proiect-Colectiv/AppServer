package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Domain.CompositeKey;

public interface ICartItemRepo extends Repository<CompositeKey<Integer,Integer>, CartItem>{
    Iterable<CartItem> findAllForOrder(String orderID);
    void clearCart(String orderID);
}