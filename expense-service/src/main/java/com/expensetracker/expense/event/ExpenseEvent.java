package com.expensetracker.expense.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEvent {

    private Long expenseId;
    private String userId;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
    private LocalDateTime eventTimestamp;

}
