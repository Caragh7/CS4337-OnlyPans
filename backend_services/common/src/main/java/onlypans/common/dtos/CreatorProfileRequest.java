package onlypans.common.dtos;

public class CreatorProfileRequest {
    private Long userId;
    private String firstName;
    private String lastName;

    // Default constructor
    public CreatorProfileRequest() {}

    // Parameterized constructor
    public CreatorProfileRequest(Long userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
}

