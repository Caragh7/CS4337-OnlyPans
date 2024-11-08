package onlypans.postService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Post {
// fields for post class, getters and setters
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

}
