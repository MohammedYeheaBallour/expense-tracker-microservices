package com.expensetracker.expense.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseEvent {

    private Long expenseId;
    private String userId;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
    private LocalDateTime eventTimestamp;

    public ExpenseEvent() {
    }

    public ExpenseEvent(Long expenseId, String userId, BigDecimal amount, String category, LocalDate expenseDate, LocalDateTime eventTimestamp) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.eventTimestamp = eventTimestamp;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

}
