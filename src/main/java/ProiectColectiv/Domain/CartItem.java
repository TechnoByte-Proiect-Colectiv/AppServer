package ProiectColectiv.Domain;

public class CartItem extends Entity<CompositeKey<String,Integer>> {
    private String idOrder;
    private Integer productID;
    private Integer nrOrdered;

    public CartItem(String idOrder, Integer productID, Integer nrOrdered) {
        super.setId(new CompositeKey<>(idOrder, productID));

        this.idOrder = idOrder;
        this.productID = productID;
        this.nrOrdered = nrOrdered;
    }

    public String getOrderID() {
        return idOrder;
    }

    public void setOrderID(String idOrder) {
        this.idOrder = idOrder;
        if (this.productID != null) {
            super.setId(new CompositeKey<>(idOrder, this.productID));
        }
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
        if (this.idOrder != null) {
            super.setId(new CompositeKey<>(this.idOrder, productID));
        }
    }

    public Integer getNrOrdered() {
        return nrOrdered;
    }

    public void setNrOrdered(Integer nrOrdered) {
        this.nrOrdered = nrOrdered;
    }
}