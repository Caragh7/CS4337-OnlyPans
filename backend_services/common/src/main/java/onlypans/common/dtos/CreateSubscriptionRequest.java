package onlypans.common.dtos;

public class CreateSubscriptionRequest {
    private String userId;
    private String from;

    public CreateSubscriptionRequest(String userId, String from) {
        this.userId = userId;
        this.from = from;
    }

    public CreateSubscriptionRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
