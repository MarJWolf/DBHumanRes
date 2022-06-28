package com.marti.humanresbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class HumanResBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanResBackendApplication.class, args);
    }

}
