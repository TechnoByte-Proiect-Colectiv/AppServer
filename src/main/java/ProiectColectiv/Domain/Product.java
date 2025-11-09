package ProiectColectiv.Domain;

public class Product extends Entity<Integer>
{
    private String name;
    private String description;
    private Float price;
    private Integer quantity;
    private Integer nrSold;
    private byte[] fileData;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
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

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] filePath) {
        this.fileData = filePath;
    }

    public Product(String name, String description, Float price, Integer quantity, Integer nrSold, byte[] fileData) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.nrSold = nrSold;
        this.fileData = fileData;
    }

    public Product(String name, String description, Float price, Integer quantity, Integer nrSold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.nrSold = nrSold;
    }
}
