package com.prsanmartin.appmartin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AppmartinApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppmartinApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Backend funcionando correctamente!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}