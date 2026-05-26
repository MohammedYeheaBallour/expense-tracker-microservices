package com.expensetracker.budget.repository;

import com.expensetracker.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.userId = :userId")
    List<Budget> findByUserId(@Param("userId") String userId);

    @Query("SELECT b FROM Budget b WHERE b.userId = :userId AND b.month = :month")
    Optional<Budget> findByUserIdAndMonth(@Param("userId") String userId, @Param("month") String month);

}
