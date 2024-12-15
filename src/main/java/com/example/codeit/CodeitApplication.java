package com.example.codeit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CodeitApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodeitApplication.class, args);
	}
}
