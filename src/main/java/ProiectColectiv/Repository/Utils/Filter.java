package ProiectColectiv.Repository.Utils;

public class Filter {
    private Float priceLower;
    private Float priceHigher;
    private Boolean filterInStock;

    public Filter(Float priceLower, Float priceHigher, Boolean filterInStock) {
        this.priceLower = priceLower;
        this.priceHigher = priceHigher;
        this.filterInStock = filterInStock;
    }

    public void setFilterInStock() {
        this.filterInStock = true;
    }

    public void setPriceLower(float priceLower) {
        this.priceLower = priceLower;
    }

    public void setPriceHigher(float priceHigher) {
        this.priceHigher = priceHigher;
    }

    public Boolean isFilterInStock() {
        return filterInStock;
    }

    public Float getPriceLower() {
        return priceLower;
    }

    public Float getPriceHigher() {
        return priceHigher;
    }

    public Filter(){}
}
