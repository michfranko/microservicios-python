package com.example.products;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(null, "Producto 1", "Descripción 1", 10.0, 100));
                productRepository.save(new Product(null, "Producto 2", "Descripción 2", 20.0, 50));
                productRepository.save(new Product(null, "Producto 3", "Descripción 3", 30.0, 25));
            }
        };
    }
}