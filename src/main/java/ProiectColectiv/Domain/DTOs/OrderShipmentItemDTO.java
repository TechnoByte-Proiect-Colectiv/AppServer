package ProiectColectiv.Domain.DTOs;

public class OrderShipmentItemDTO {
    private Integer productId;
    private String productName;
    private String picture;
    private Integer quantity;
    private Float unitPrice;
    private String currency;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Float unitPrice) { this.unitPrice = unitPrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

}