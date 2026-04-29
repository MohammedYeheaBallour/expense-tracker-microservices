package com.expensetracker.expense.service;

import com.expensetracker.expense.client.CategoryServiceClient;
import com.expensetracker.expense.entity.Expense;
import com.expensetracker.expense.event.ExpenseEvent;
import com.expensetracker.expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryServiceClient categoryServiceClient;

    @Autowired(required = false)
    private KafkaTemplate<String, ExpenseEvent> kafkaTemplate;

    public Expense createExpense(Expense expense) {
        // Validate category with Category Service (REST call)
        if (!categoryServiceClient.validateCategory(expense.getCategory())) {
            throw new RuntimeException("Invalid category: " + expense.getCategory());
        }

        Expense savedExpense = expenseRepository.save(expense);

        // Publish event to Kafka
        if (kafkaTemplate != null) {
            ExpenseEvent event = new ExpenseEvent(
                    savedExpense.getId(),
                    savedExpense.getUserId(),
                    savedExpense.getAmount(),
                    savedExpense.getCategory(),
                    savedExpense.getDate(),
                    LocalDateTime.now()
            );
            kafkaTemplate.send("expense-events", event.getUserId(), event);
        }

        return savedExpense;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> getExpensesByUserId(String userId) {
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getExpensesByUserIdAndCategory(String userId, String category) {
        return expenseRepository.findByUserIdAndCategory(userId, category);
    }

    public Expense updateExpense(Long id, Expense expenseDetails) {
        // Validate category if changed
        if (!categoryServiceClient.validateCategory(expenseDetails.getCategory())) {
            throw new RuntimeException("Invalid category: " + expenseDetails.getCategory());
        }

        return expenseRepository.findById(id)
                .map(expense -> {
                    expense.setAmount(expenseDetails.getAmount());
                    expense.setCategory(expenseDetails.getCategory());
                    expense.setDate(expenseDetails.getDate());
                    expense.setNote(expenseDetails.getNote());
                    return expenseRepository.save(expense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

}
