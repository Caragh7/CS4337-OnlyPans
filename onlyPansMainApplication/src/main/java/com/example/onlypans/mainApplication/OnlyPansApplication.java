package com.example.onlypans.mainApplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example.onlypans")
@RestController
public class OnlyPansApplication {


	public static void main(String[] args) {
		SpringApplication.run(OnlyPansApplication.class, args);
	}

}
