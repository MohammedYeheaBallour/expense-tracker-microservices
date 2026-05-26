package com.expensetracker.dashboard.listener;

import com.expensetracker.dashboard.graphql.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.math.BigDecimal;

@Service
public class ExpenseEventListener {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "expense-events", groupId = "dashboard-service-group", autoStartup = "true")
    public void handleExpenseEvent(String message) {
        System.out.println("ExpenseEventListener received message: " + message);
        try {
            Map<String, Object> eventMap = objectMapper.readValue(message, Map.class);
            System.out.println("Parsed event map: " + eventMap);
            
            String userId = (String) eventMap.get("userId");
            Object amountObj = eventMap.get("amount");
            Double amount = (amountObj instanceof Number) ? ((Number) amountObj).doubleValue() : Double.parseDouble(amountObj.toString());
            String category = (String) eventMap.get("category");
            Object dateObj = eventMap.get("expenseDate");
            String dateStr = dateObj != null ? dateObj.toString() : (String) eventMap.get("date");
            
            if (dateStr == null) {
                System.err.println("Date field not found in event: " + eventMap.keySet());
                return;
            }

            // Extract month from date (YYYY-MM-DD format or ISO format)
            String month = dateStr.length() >= 7 ? dateStr.substring(0, 7) : dateStr;

            dashboardService.addExpense(userId, month, category, BigDecimal.valueOf(amount));
            System.out.println("✅ Expense event processed for user: " + userId + ", amount: " + amount + ", month: " + month);
        } catch (Exception e) {
            System.err.println("❌ Error processing expense event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
