package onlypans.common.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "CreatorProfile")
public class CreatorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId; // Foreign key to User entity, how to connect these??
    private String firstName;
    private String lastName;
    private String stripePriceId;
    private Float revenue;

    // Default constructor
    public CreatorProfile() {}

    // New constructor with arguments for userId, firstName, and lastName
    public CreatorProfile(String userId, String firstName, String lastName, String stripePriceId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.stripePriceId = stripePriceId;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStripePriceId() { return stripePriceId; }
    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public Float getRevenue() { return revenue; }
    public void setRevenue(Float revenue) { this.revenue = revenue; }

}

