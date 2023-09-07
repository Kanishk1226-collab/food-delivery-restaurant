package com.example.food.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
public class FoodDeliveryRestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryRestaurantApplication.class, args);
	}
}
