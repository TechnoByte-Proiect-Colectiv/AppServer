package ProiectColectiv.Domain;
import com.google.gson.Gson;

public class Product extends Entity<Integer>
{
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
    private Integer nrSold;
    /// private Integer idSeller;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getNrSold() {
        return nrSold;
    }

    public void setNrSold(Integer nrSold) {
        this.nrSold = nrSold;
    }

    public Product(String name, String description, Integer price, Integer quantity, Integer nrSold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.nrSold = nrSold;
    }
}
