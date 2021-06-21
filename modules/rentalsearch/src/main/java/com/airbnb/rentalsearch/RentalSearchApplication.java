package com.airbnb.rentalsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.airbnb"})
public class RentalSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalSearchApplication.class, args);
    }

}
