package com.example.onlypans.mainApplication;

import com.example.onlypans.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example.onlypans")
@RestController
public class OnlyPansApplication {

	private final UserService userSvc;

	public OnlyPansApplication(UserService userSvc) {
		this.userSvc = userSvc;
	}

	@GetMapping("/user")
	public String home() {
		return userSvc.message();
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlyPansApplication.class, args);
	}

}
