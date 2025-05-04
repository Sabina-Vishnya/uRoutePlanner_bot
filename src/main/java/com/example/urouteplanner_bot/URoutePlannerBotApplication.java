package com.example.urouteplanner_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class URoutePlannerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(URoutePlannerBotApplication.class, args);
    }

}
