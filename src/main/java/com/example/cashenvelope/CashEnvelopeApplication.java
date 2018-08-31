package com.example.cashenvelope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CashEnvelopeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashEnvelopeApplication.class, args);
	}
}
