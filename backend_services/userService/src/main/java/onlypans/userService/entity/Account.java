package onlypans.userService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Account") // Specify the exact table name here
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented primary key
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false) // Foreign key to User.id
    private User user;


    @Column(nullable = true)
    private String refresh_token;

    @Column(nullable = true)
    private String access_token;

    // Getters and Setters
    public Long getId() {
        return user.getId();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public String getRefreshToken() {
//        return refreshToken;
//    }
//
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
}
