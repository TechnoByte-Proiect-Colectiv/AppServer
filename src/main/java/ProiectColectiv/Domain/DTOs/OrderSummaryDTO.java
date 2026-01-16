package ProiectColectiv.Domain.DTOs;

import java.time.LocalDate;
import java.util.List;

public class OrderSummaryDTO {
    private Integer id;
    private LocalDate createdAt;
    private Float total;
    private String currency;
    private List<String> previewImages;

    public OrderSummaryDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public Float getTotal() { return total; }
    public void setTotal(Float total) { this.total = total; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public List<String> getPreviewImages() { return previewImages; }
    public void setPreviewImages(List<String> previewImages) { this.previewImages = previewImages; }
}