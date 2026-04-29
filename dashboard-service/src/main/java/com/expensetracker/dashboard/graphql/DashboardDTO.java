package com.expensetracker.dashboard.graphql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private String userId;
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal monthlyBudget;
    private BigDecimal remainingBudget;
    private List<CategoryExpenseDTO> expensesByCategory;
    private List<SourceIncomeDTO> incomesBySource;

}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CategoryExpenseDTO {
    private String category;
    private BigDecimal amount;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SourceIncomeDTO {
    private String source;
    private BigDecimal amount;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
}
