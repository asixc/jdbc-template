package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(JdbcApplication.class, args);

		JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

		jdbcTemplate.getDataSource(); // Base de datos



	}

}
