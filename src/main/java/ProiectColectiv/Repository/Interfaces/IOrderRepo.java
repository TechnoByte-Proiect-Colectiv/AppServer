package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;

public interface IOrderRepo extends  Repository<CompositeKey<String,Integer>, Order>{
    Iterable<Order> findAllForUser(String userID);
}
