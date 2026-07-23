package com.ankit.learn_springboot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnSpringbootApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach((dotenvEntry -> System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())));
		System.out.println("DB URL: " + System.getProperty("DB_URL"));
		System.out.println("DB_USERNAME: " + System.getProperty("DB_USERNAME"));
		System.out.println("DB_PASSWORD length: " + System.getProperty("DB_PASSWORD").length());
		SpringApplication app = new SpringApplication(LearnSpringbootApplication.class);



		app.run(args);
	}
}