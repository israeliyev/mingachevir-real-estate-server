package io.mingachevir.mingachevirrealestateserver;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "io.mingachevir.mingachevirrealestateserver.repository")
//@EntityScan(basePackages = "io.mingachevir.mingachevirrealestateserver.model.entity")
public class MingachevirRealEstateAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MingachevirRealEstateAgencyApplication.class, args);
    }
}
