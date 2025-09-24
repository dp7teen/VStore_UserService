package com.dp.vstore_userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class VStoreUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VStoreUserServiceApplication.class, args);
    }

}
