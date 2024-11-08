package onlypans.postService.repository;

import onlypans.postService.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
 // can implement custom queries here if we want (outside of standard CRUD operations JPA provides)
    // creating this repository means we can use JPA to interact with database - dont have to hardcode SQL queries, we can just use JPA methods/endpoints
}