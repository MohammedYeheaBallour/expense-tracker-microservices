package com.expensetracker.income.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class IncomeEvent {

    private Long incomeId;
    private String userId;
    private BigDecimal amount;
    private String source;
    private LocalDate incomeDate;
    private LocalDateTime eventTimestamp;

    public IncomeEvent() {
    }

    public IncomeEvent(Long incomeId, String userId, BigDecimal amount, String source, LocalDate incomeDate, LocalDateTime eventTimestamp) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.amount = amount;
        this.source = source;
        this.incomeDate = incomeDate;
        this.eventTimestamp = eventTimestamp;
    }

    // Getters
    public Long getIncomeId() {
        return incomeId;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    // Setters
    public void setIncomeId(Long incomeId) {
        this.incomeId = incomeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

}
