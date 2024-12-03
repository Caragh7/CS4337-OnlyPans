package onlypans.common.dtos;

public class CreatorProfileRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String price;

    // Default constructor
    public CreatorProfileRequest() {
    }

    // Parameterized constructor
    public CreatorProfileRequest(String userId, String firstName, String lastName, String price) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.price = price;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

}

