package onlypans.engagementService.clients;

import onlypans.postService.entity.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service", url = "http://post-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface PostServiceClient {

    @GetMapping("/posts/{id}")
    Post getPostById(@PathVariable Long id);
}
