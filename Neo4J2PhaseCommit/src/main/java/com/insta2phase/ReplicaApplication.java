package com.insta2phase;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EntityScan(basePackages = {"com.insta2phase.entities", "com.insta2phase.dal.utils"})
@SpringBootApplication
public class ReplicaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ReplicaApplication.class)
                .properties("spring.config.name:replica")
                .build()
                .run(args);
    }
}
