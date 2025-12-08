package ProiectColectiv.Domain;

import java.time.LocalDate;

public class Order extends Entity<CompositeKey<String,Integer>>{
    private String userID;
    private Integer productID;
    private Integer quantity;
    private LocalDate orderDate;      // created at
    private Float totalProducts;
    private Float totalShipping;
    private Float totalPrice;
    private String paymentMethod;
    private Boolean paymentStatus;
    private String deliveryStatus;    // order status
    private String address;

    public Order(String userID, Integer productID, Integer quantity, LocalDate orderDate,
                 Float totalProducts, Float totalShipping, Float totalPrice,
                 String paymentMethod, Boolean paymentStatus, String deliveryStatus, String address) {

        super.setId(new CompositeKey<>(userID, productID));

        this.userID = userID;
        this.productID = productID;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.totalProducts = totalProducts;
        this.totalShipping = totalShipping;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
        this.address = address;
    }

    public Order() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        if (this.productID != null) {
            super.setId(new CompositeKey<>(userID, this.productID));
        }
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
        if (this.userID != null) {
            super.setId(new CompositeKey<>(this.userID, productID));
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Float getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Float totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Float getTotalShipping() {
        return totalShipping;
    }

    public void setTotalShipping(Float totalShipping) {
        this.totalShipping = totalShipping;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}