package ProiectColectiv.Domain;

public class Product extends Entity<Integer> {
    private String name;
    private String description;
    private String slug;      // url
    private String brand;
    private Float price;
    private Integer quantity; // stock
    private String currency;
    private String image;     // path sau url
    private Integer nrSold;
    private String category;  // type
    private byte[] fileData;
    // private Integer idSeller;

    public Product(String name, String description, String slug, String brand, Float price,
                   Integer quantity, String currency, String image, Integer nrSold,
                   String category, byte[] fileData) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.currency = currency;
        this.image = image;
        this.nrSold = nrSold;
        this.category = category;
        this.fileData = fileData;
    }

    public Product() {
    }

    public Product(String name, String description, Float price, Integer quantity, Integer nrSold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.nrSold = nrSold;
    }

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNrSold() {
        return nrSold;
    }

    public void setNrSold(Integer nrSold) {
        this.nrSold = nrSold;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}