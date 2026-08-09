package com.hacklink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackLinkApplication.class, args);
    }
}
