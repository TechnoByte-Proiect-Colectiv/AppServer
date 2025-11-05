package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.util.Properties;

public class ProductRepo implements IProductRepo {
    private final JdbcUtils dbUtils;

    public ProductRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<Product> findAll() {
        return null;
    }

    @Override
    public Product findById(Integer integer) {
        return null;
    }

    @Override
    public void update(Product entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void save(Product entity) {

    }
}
