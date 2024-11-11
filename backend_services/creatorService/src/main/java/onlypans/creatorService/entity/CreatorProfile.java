package onlypans.creatorService.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "CreatorProfile")
public class CreatorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Long userId; // Foreign key to User entity, how to connect these??
    private String firstName;
    private String lastName;


    // Default constructor
    public CreatorProfile() {}

    // New constructor with arguments for userId, firstName, and lastName
    public CreatorProfile(Long userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

}


