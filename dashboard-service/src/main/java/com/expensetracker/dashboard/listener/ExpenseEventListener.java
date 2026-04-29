package com.expensetracker.dashboard.listener;

import com.expensetracker.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
public class ExpenseEventListener {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "expense-events", groupId = "dashboard-service-group")
    public void handleExpenseEvent(String message) {
        try {
            Map<String, Object> eventMap = objectMapper.readValue(message, Map.class);
            String userId = (String) eventMap.get("userId");
            Double amount = ((Number) eventMap.get("amount")).doubleValue();
            String category = (String) eventMap.get("category");
            String dateStr = (String) eventMap.get("expenseDate");

            // Extract month from date (YYYY-MM-DD format)
            String month = dateStr.substring(0, 7);

            dashboardService.addExpense(userId, month, category, java.math.BigDecimal.valueOf(amount));
            System.out.println("Expense event processed for user: " + userId + ", amount: " + amount);
        } catch (Exception e) {
            System.err.println("Error processing expense event: " + e.getMessage());
        }
    }

}
