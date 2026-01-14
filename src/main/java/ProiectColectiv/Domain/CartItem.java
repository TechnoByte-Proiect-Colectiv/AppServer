package ProiectColectiv.Domain;

public class CartItem extends Entity<CompositeKey<Integer,Integer>> {
    private Integer idOrder;
    private Integer productID;
    private Integer nrOrdered;

    public CartItem(Integer idOrder, Integer productID, Integer nrOrdered) {
        super.setId(new CompositeKey<>(idOrder, productID));

        this.idOrder = idOrder;
        this.productID = productID;
        this.nrOrdered = nrOrdered;
    }

    public CartItem() {
    }

    public Integer getOrderID() {
        return idOrder;
    }

    public void setOrderID(Integer idOrder) {
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