package com.expensetracker.expense.repository;

import com.expensetracker.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(String userId);

    List<Expense> findByUserIdAndCategory(String userId, String category);

    List<Expense> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

}
