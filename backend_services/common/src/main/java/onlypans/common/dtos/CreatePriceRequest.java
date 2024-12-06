package onlypans.common.dtos;

public class CreatePriceRequest {
    private String creatorProfileId;
    private String price;
    private String creatorName;

    public CreatePriceRequest() {};

    public CreatePriceRequest(String creatorProfileId, String price, String creatorName) {
        this.creatorProfileId = creatorProfileId;
        this.price = price;
        this.creatorName = creatorName;
    }

    public String getCreatorProfileId() {
        return creatorProfileId;
    }

    public void setCreatorProfileId(String creatorProfileId) {
        this.creatorProfileId = creatorProfileId;
    }

    public void setCreatorName (String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
