package ProiectColectiv.Domain.DTOs;

import ProiectColectiv.Domain.Address;

import java.time.LocalDate;
import java.util.List;

public class OrderResponseDTO {
    private Integer id;
    private LocalDate createdAt;
    private Float totalProducts;
    private Float totalShipping;
    private Float total;
    private String currency;
    private String paymentMethod;
    private Boolean paymentStatus;
    private String orderStatus;

    private Address billingAddress;
    private Address shippingAddress;

    private List<OrderShipmentDTO> shipments;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public Float getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Float totalProducts) { this.totalProducts = totalProducts; }
    public Float getTotalShipping() { return totalShipping; }
    public void setTotalShipping(Float totalShipping) { this.totalShipping = totalShipping; }
    public Float getTotal() { return total; }
    public void setTotal(Float total) { this.total = total; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Boolean getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(Boolean paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public Address getBillingAddress() { return billingAddress; }
    public void setBillingAddress(Address billingAddress) { this.billingAddress = billingAddress; }
    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public List<OrderShipmentDTO> getShipments() { return shipments; }
    public void setShipments(List<OrderShipmentDTO> shipments) { this.shipments = shipments; }
}