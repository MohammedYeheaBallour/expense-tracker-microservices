package com.expensetracker.dashboard.graphql;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {

    private String userId;
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal monthlyBudget;
    private BigDecimal remainingBudget;
    private List<CategoryExpenseDTO> expensesByCategory;
    private List<SourceIncomeDTO> incomesBySource;

    public DashboardDTO() {
    }

    public DashboardDTO(String userId, String month, BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal monthlyBudget, BigDecimal remainingBudget, List<CategoryExpenseDTO> expensesByCategory, List<SourceIncomeDTO> incomesBySource) {
        this.userId = userId;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.monthlyBudget = monthlyBudget;
        this.remainingBudget = remainingBudget;
        this.expensesByCategory = expensesByCategory;
        this.incomesBySource = incomesBySource;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(BigDecimal monthlyBudget) { this.monthlyBudget = monthlyBudget; }
    public BigDecimal getRemainingBudget() { return remainingBudget; }
    public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }
    public List<CategoryExpenseDTO> getExpensesByCategory() { return expensesByCategory; }
    public void setExpensesByCategory(List<CategoryExpenseDTO> expensesByCategory) { this.expensesByCategory = expensesByCategory; }
    public List<SourceIncomeDTO> getIncomesBySource() { return incomesBySource; }
    public void setIncomesBySource(List<SourceIncomeDTO> incomesBySource) { this.incomesBySource = incomesBySource; }

    public static class Builder {
        private String userId;
        private String month;
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal monthlyBudget;
        private BigDecimal remainingBudget;
        private List<CategoryExpenseDTO> expensesByCategory;
        private List<SourceIncomeDTO> incomesBySource;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder month(String month) { this.month = month; return this; }
        public Builder totalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; return this; }
        public Builder totalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; return this; }
        public Builder monthlyBudget(BigDecimal monthlyBudget) { this.monthlyBudget = monthlyBudget; return this; }
        public Builder remainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; return this; }
        public Builder expensesByCategory(List<CategoryExpenseDTO> expensesByCategory) { this.expensesByCategory = expensesByCategory; return this; }
        public Builder incomesBySource(List<SourceIncomeDTO> incomesBySource) { this.incomesBySource = incomesBySource; return this; }

        public DashboardDTO build() {
            return new DashboardDTO(userId, month, totalIncome, totalExpenses, monthlyBudget, remainingBudget, expensesByCategory, incomesBySource);
        }
    }

}

class CategoryExpenseDTO {
    private String category;
    private BigDecimal amount;

    public CategoryExpenseDTO() {
    }

    public CategoryExpenseDTO(String category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

class SourceIncomeDTO {
    private String source;
    private BigDecimal amount;

    public SourceIncomeDTO() {
    }

    public SourceIncomeDTO(String source, BigDecimal amount) {
        this.source = source;
        this.amount = amount;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

class SummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;

    public SummaryDTO() {
    }

    public SummaryDTO(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
    }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getNetBalance() { return netBalance; }
    public void setNetBalance(BigDecimal netBalance) { this.netBalance = netBalance; }
}
