package ProiectColectiv.Domain.DTOs;
import java.util.List;

public class OrderShipmentDTO {
    private String sellerId;
    private String sellerName;
    private String status;
    private Float shippingCost;
    private String shippingCurrency;
    private List<OrderShipmentItemDTO> items;

    public OrderShipmentDTO() {
        this.sellerId = "1";
        this.sellerName = "Main Store";
        this.status = "Pending";
        this.shippingCost = 0f;
        this.shippingCurrency = "RON";
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getShippingCurrency() {
        return shippingCurrency;
    }

    public void setShippingCurrency(String shippingCurrency) {
        this.shippingCurrency = shippingCurrency;
    }

    public List<OrderShipmentItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderShipmentItemDTO> items) {
        this.items = items;
    }
}