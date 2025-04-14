package com.globits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.globits")
public class DAApplication {
	public static void main(String[] args) {
		SpringApplication.run(DAApplication.class, args);
	}
}
