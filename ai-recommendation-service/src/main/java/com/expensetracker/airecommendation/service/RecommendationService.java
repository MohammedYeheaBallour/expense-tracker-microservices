package com.expensetracker.airecommendation.service;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    public String generateAdvice(double monthlyBudget, double currentExpenses) {
        double spendingPercentage = (currentExpenses / monthlyBudget) * 100;

        if (spendingPercentage >= 100) {
            return "ALERT: You have exceeded your budget! Current spending is " + String.format("%.2f", spendingPercentage) + "% of your budget.";
        } else if (spendingPercentage >= 80) {
            return "WARNING: You are close to your budget limit (" + String.format("%.2f", spendingPercentage) + "%). Consider reducing expenses.";
        } else if (spendingPercentage >= 50) {
            return "MODERATE: You are spending " + String.format("%.2f", spendingPercentage) + "% of your budget. Keep tracking.";
        } else {
            return "GOOD: You are spending well within your budget (" + String.format("%.2f", spendingPercentage) + "%). Good financial discipline!";
        }
    }

    public double calculateSpendingPercentage(double monthlyBudget, double currentExpenses) {
        return (currentExpenses / monthlyBudget) * 100;
    }

    public boolean isWithinBudget(double monthlyBudget, double currentExpenses) {
        return currentExpenses <= monthlyBudget;
    }

}
