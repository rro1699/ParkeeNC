package com.example.ncproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

@SpringBootApplication
public class NCprojectApplication {

    public static void main(String[] args) {

        // SpringApplication.run(NCprojectApplication.class, args);
        SpringApplication app = new SpringApplication(NCprojectApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8090"));

        app.run(args);
    }

}
