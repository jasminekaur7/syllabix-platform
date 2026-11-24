package com.syllabix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SyllabixApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyllabixApplication.class, args);
        System.out.println("========================================================================");
        System.out.println(">>> Syllabix: Intelligent Academic Platform is UP and RUNNING! <<<");
        System.out.println(">>> Local Web Dashboard: http://localhost:8080/                      <<<");
        System.out.println(">>> Database Console:    http://localhost:8080/h2-console            <<<");
        System.out.println("========================================================================");
    }
}
