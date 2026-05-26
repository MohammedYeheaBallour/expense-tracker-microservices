package com.expensetracker.income.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "incomes")
@NoArgsConstructor
@AllArgsConstructor
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    // Getters
    public Long getId() {
        return id;
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

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

}
