package com.rectle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RectleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RectleServiceApplication.class, args);
	}

}
