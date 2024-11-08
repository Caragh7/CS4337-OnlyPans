package onlypans.creatorService.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "creator_profile")
public class CreatorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String userId; // Foreign key to User entity, how to connect these??
    private String firstName;
    private String lastName;

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserId() { return userId; }
    public void setPassword(String userId) { this.userId = userId; }
}


