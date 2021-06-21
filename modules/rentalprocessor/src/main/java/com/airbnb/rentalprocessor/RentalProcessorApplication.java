package com.airbnb.rentalprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.airbnb"})
public class RentalProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalProcessorApplication.class, args);
    }

}
