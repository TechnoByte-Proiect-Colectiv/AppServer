package ProiectColectiv.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Review extends Entity<CompositeKey<Integer,String>>{
    private Integer productId;
    private String userId;
    private int rating;
    private String title;
    private String description;
    private LocalDate created_at;
    private Boolean verifiedPurchase;

    @Override
    @JsonProperty("id")
    public CompositeKey<Integer, String> getId() {
        if (super.getId() == null && productId != null && userId != null) {
            super.setId(new CompositeKey<>(productId, userId));
        }
        return super.getId();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Boolean getVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public Review() {

    }

    public Review(String userId, Integer productId, int rating, String title, String description, LocalDate created_at, Boolean verifiedPurchase) {
        super.setId(new CompositeKey<>(productId,userId));
        this.userId=userId;
        this.productId=productId;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.verifiedPurchase = verifiedPurchase;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
        if(this.userId!=null){
            super.setId(new CompositeKey<>(productId,this.userId));
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        if(this.productId!=null){
            super.setId(new CompositeKey<>(this.productId,userId));
        }
    }
}

