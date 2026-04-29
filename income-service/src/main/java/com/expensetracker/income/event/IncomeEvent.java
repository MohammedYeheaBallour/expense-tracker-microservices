package com.expensetracker.income.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeEvent {

    private Long incomeId;
    private String userId;
    private BigDecimal amount;
    private String source;
    private LocalDate incomeDate;
    private LocalDateTime eventTimestamp;

}
