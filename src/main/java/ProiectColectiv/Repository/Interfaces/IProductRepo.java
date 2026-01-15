package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Utils.Filter;

public interface IProductRepo extends Repository<Integer, Product> {
    Iterable<Product> getMostSoldProducts(int nr);
    Iterable<Product> filteredSearch(Filter filter,String searchInput);
    Iterable<Product> getAllProducts();
    Product findBySlug(String slug);
    void updateStock(Integer productId, Integer quantitySold);
}
