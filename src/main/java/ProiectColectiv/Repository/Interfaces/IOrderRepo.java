package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.Order;

public interface IOrderRepo extends  Repository<Integer, Order>{
    Iterable<Order> getAllOrdersByUser(String idUser);
    Integer saveWithReturn(Order entity);
    Iterable<Order> findAll();
}
