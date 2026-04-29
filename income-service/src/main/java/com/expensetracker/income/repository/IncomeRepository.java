package com.expensetracker.income.repository;

import com.expensetracker.income.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(String userId);

    List<Income> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

}
