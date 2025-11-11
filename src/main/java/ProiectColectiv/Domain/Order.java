package ProiectColectiv.Domain;

public class Order extends Entity<CompositeKey<String,Integer>>{
    private String userID;
    private Integer productID;
    private Integer quantity;
    private String deliveryStatus;

    public Order(String userID, Integer productID, Integer quantity, String deliveryStatus) {
        super.setId(new CompositeKey<>(userID,productID));
        this.quantity = quantity;
        this.deliveryStatus = deliveryStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
