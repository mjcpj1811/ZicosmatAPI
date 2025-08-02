package com.example.ZicosmatAPI;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZicosmatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZicosmatApiApplication.class, args);
	}
}
