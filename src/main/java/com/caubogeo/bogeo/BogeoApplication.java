package com.caubogeo.bogeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BogeoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BogeoApplication.class, args);
    }

}
