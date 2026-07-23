package com.grepo.grepobackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GithubSEngineApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach((dotenvEntry -> System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())));
        System.out.println("DB URL: " + System.getProperty("DB_URL"));
        System.out.println("DB_USERNAME: " + System.getProperty("DB_USERNAME"));
        System.out.println("DB_PASSWORD length: " + System.getProperty("DB_PASSWORD").length());
        SpringApplication app = new SpringApplication(GithubSEngineApplication.class);

        app.run(args);
    }

}
