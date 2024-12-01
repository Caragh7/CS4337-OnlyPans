package onlypans.emailNotificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class EmailNotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailNotificationServiceApplication.class, args);
    }
}





