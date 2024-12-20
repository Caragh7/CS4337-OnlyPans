package onlypans.creatorService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CreatorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreatorServiceApplication.class, args);
    }
}
