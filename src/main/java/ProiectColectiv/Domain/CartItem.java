package ProiectColectiv.Domain;

public class CartItem extends Entity<CompositeKey<String,Integer>>{
    private String userID;
    private Integer productID;
    private Integer nrOrdered;

    public CartItem(String userID, Integer productID, Integer nrOrdered) {
        super.setId(new CompositeKey<>(userID,productID));
        this.nrOrdered = nrOrdered;
    }

    public String getIdUser() {
        return userID;
    }

    public void setIdUser(String userID) {
        this.userID = userID;
    }

    public Integer getIdProduct() {
        return productID;
    }

    public void setIdProduct(Integer productID) {
        this.productID = productID;
    }

    public Integer getNrOrdered() {
        return nrOrdered;
    }

    public void setNrOrdered(Integer nrOrdered) {
        this.nrOrdered = nrOrdered;
    }
}
