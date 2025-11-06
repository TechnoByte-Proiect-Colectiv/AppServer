package ProiectColectiv.Repository.Utils;

public class Filter {
    private boolean filterPriceLower;
    private boolean filterPriceHigher;
    private int priceLower;
    private int priceHigher;
    private boolean filterInStock;

    public void setFilterPriceLower(int price) {
        this.priceLower = price;
        filterPriceLower = true;
    }

    public void setFilterPriceHigher(int price) {
        this.priceHigher = price;
        filterPriceHigher = true;
    }

    public void setFilterInStock() {
        this.filterInStock = true;
    }

    public boolean isFilterPriceLower() {
        return filterPriceLower;
    }

    public boolean isFilterPriceHigher() {
        return filterPriceHigher;
    }

    public boolean isFilterInStock() {
        return filterInStock;
    }

    public int getPriceLower() {
        return priceLower;
    }

    public int getPriceHigher() {
        return priceHigher;
    }
}
