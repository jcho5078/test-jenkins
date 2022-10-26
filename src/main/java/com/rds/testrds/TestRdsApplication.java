package com.rds.testrds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class TestRdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestRdsApplication.class, args);
    }

}
