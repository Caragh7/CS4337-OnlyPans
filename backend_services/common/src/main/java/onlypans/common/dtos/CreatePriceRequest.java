package onlypans.common.dtos;

public class CreatePriceRequest {
    private String creatorProfileId;
    private String price;

    public String getCreatorProfileId() {
        return creatorProfileId;
    }

    public void setCreatorProfileId(String creatorProfileId) {
        this.creatorProfileId = creatorProfileId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
