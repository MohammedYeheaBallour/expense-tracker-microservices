package com.expensetracker.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.expensetracker.budget.repository")
public class BudgetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetServiceApplication.class, args);
    }

}
