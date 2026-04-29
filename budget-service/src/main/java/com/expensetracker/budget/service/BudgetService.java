package com.expensetracker.budget.service;

import com.expensetracker.budget.entity.Budget;
import com.expensetracker.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    public List<Budget> getBudgetsByUserId(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    public Optional<Budget> getBudgetByUserIdAndMonth(String userId, String month) {
        return budgetRepository.findByUserIdAndMonth(userId, month);
    }

    public Budget updateBudget(Long id, Budget budgetDetails) {
        return budgetRepository.findById(id)
                .map(budget -> {
                    budget.setTotalLimit(budgetDetails.getTotalLimit());
                    budget.setMonth(budgetDetails.getMonth());
                    return budgetRepository.save(budget);
                })
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

}
