package onlypans.subscriptionService.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    // fields for post class, getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String userId;

    @Column()
    private String creatorProfileId;

    @Column()
    private String stripeSubscriptionId;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCreatorProfileId() { return creatorProfileId; }
    public void setCreatorProfileId(String creatorProfileId) { this.creatorProfileId = creatorProfileId; }

    public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
