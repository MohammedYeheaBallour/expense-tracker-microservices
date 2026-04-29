package com.expensetracker.dashboard.service;

import com.expensetracker.dashboard.graphql.CategoryExpenseDTO;
import com.expensetracker.dashboard.graphql.DashboardDTO;
import com.expensetracker.dashboard.graphql.SummaryDTO;
import com.expensetracker.dashboard.graphql.SourceIncomeDTO;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {

    private final Map<String, Map<String, Object>> userDataStore = new ConcurrentHashMap<>();

    public void addExpense(String userId, String month, String category, BigDecimal amount) {
        String key = userId + ":" + month;
        userDataStore.putIfAbsent(key, initializeUserData());
        Map<String, Object> data = userDataStore.get(key);

        BigDecimal totalExpenses = (BigDecimal) data.get("totalExpenses");
        data.put("totalExpenses", totalExpenses.add(amount));

        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> expensesByCategory = (Map<String, BigDecimal>) data.get("expensesByCategory");
        expensesByCategory.put(category, expensesByCategory.getOrDefault(category, BigDecimal.ZERO).add(amount));
    }

    public void addIncome(String userId, String month, String source, BigDecimal amount) {
        String key = userId + ":" + month;
        userDataStore.putIfAbsent(key, initializeUserData());
        Map<String, Object> data = userDataStore.get(key);

        BigDecimal totalIncome = (BigDecimal) data.get("totalIncome");
        data.put("totalIncome", totalIncome.add(amount));

        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> incomesBySource = (Map<String, BigDecimal>) data.get("incomesBySource");
        incomesBySource.put(source, incomesBySource.getOrDefault(source, BigDecimal.ZERO).add(amount));
    }

    public void setBudget(String userId, String month, BigDecimal budgetLimit) {
        String key = userId + ":" + month;
        userDataStore.putIfAbsent(key, initializeUserData());
        Map<String, Object> data = userDataStore.get(key);
        data.put("monthlyBudget", budgetLimit);
    }

    public DashboardDTO getFinancialOverview(String userId, String month) {
        String key = userId + ":" + month;
        Map<String, Object> data = userDataStore.getOrDefault(key, initializeUserData());

        BigDecimal totalIncome = (BigDecimal) data.get("totalIncome");
        BigDecimal totalExpenses = (BigDecimal) data.get("totalExpenses");
        BigDecimal monthlyBudget = (BigDecimal) data.get("monthlyBudget");
        BigDecimal remainingBudget = monthlyBudget.subtract(totalExpenses);

        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> expensesByCategory = (Map<String, BigDecimal>) data.get("expensesByCategory");
        @SuppressWarnings("unchecked")
        Map<String, BigDecimal> incomesBySource = (Map<String, BigDecimal>) data.get("incomesBySource");

        List<CategoryExpenseDTO> expenses = expensesByCategory.entrySet().stream()
                .map(e -> CategoryExpenseDTO.builder().category(e.getKey()).amount(e.getValue()).build())
                .toList();

        List<SourceIncomeDTO> incomes = incomesBySource.entrySet().stream()
                .map(e -> SourceIncomeDTO.builder().source(e.getKey()).amount(e.getValue()).build())
                .toList();

        return DashboardDTO.builder()
                .userId(userId)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .monthlyBudget(monthlyBudget)
                .remainingBudget(remainingBudget)
                .expensesByCategory(expenses)
                .incomesBySource(incomes)
                .build();
    }

    public SummaryDTO getSummary(String userId) {
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Map<String, Object> data : userDataStore.values()) {
            if (data.keySet().stream().anyMatch(k -> k.contains(userId))) {
                totalIncome = totalIncome.add((BigDecimal) data.get("totalIncome"));
                totalExpenses = totalExpenses.add((BigDecimal) data.get("totalExpenses"));
            }
        }

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return SummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .build();
    }

    private Map<String, Object> initializeUserData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalIncome", BigDecimal.ZERO);
        data.put("totalExpenses", BigDecimal.ZERO);
        data.put("monthlyBudget", BigDecimal.valueOf(5000));
        data.put("expensesByCategory", new ConcurrentHashMap<String, BigDecimal>());
        data.put("incomesBySource", new ConcurrentHashMap<String, BigDecimal>());
        return data;
    }

}

import java.util.concurrent.ConcurrentHashMap;
