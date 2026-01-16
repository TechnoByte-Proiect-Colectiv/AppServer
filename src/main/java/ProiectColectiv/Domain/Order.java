package ProiectColectiv.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Order extends Entity<Integer> {

    private String idUser;

    private LocalDate orderDate;      // created at

    private Float totalProducts;
    private Float totalShipping;
    private Float totalPrice;

    private String currency;

    private String paymentMethod;
    private Boolean paymentStatus;
    private String deliveryStatus;    // order status

    private String billingAddress;
    private String shippingAddress;

    public Order() {}

    public Order(Integer orderId, String userID, LocalDate orderDate,
                 Float totalProducts, Float totalShipping, Float totalPrice, String currency,
                 String paymentMethod, Boolean paymentStatus, String deliveryStatus,
                 String billingAddress, String shippingAddress) {

        super.setId(orderId);
        this.idUser = userID;
        this.orderDate = orderDate;
        this.totalProducts = totalProducts;
        this.totalShipping = totalShipping;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public Integer getId() { return super.getId(); }

    public void setId(Integer orderId) { super.setId(orderId); }

    public String getUserID() {
        return idUser;
    }

    public void setUserID(String userID) {
        this.idUser = userID;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}