package ProiectColectiv.Domain.DTOs;

import java.util.List;

public class OrderRequestDTO {
    private ContactDTO contact;
    private AddressDTO billingAddress;
    private AddressDTO shippingAddress;
    private List<OrderItemDTO> items;
    private Float total;

    public OrderRequestDTO() {
    }

    public ContactDTO getContact() { return contact; }
    public void setContact(ContactDTO contact) { this.contact = contact; }
    public AddressDTO getBillingAddress() { return billingAddress; }
    public void setBillingAddress(AddressDTO billingAddress) { this.billingAddress = billingAddress; }
    public AddressDTO getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressDTO shippingAddress) { this.shippingAddress = shippingAddress; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
    public Float getTotal() { return total; }
    public void setTotal(Float total) { this.total = total; }
}





