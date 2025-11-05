package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.Product;

public interface IProductRepo extends Repository<Integer, Product> {
    public Iterable<Product> findAll();
}
