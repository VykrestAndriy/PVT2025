package com.example.lb_9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.lb_9.model")
public class Lb9Application {

    public static void main(String[] args) {
        SpringApplication.run(Lb9Application.class, args);
    }

}