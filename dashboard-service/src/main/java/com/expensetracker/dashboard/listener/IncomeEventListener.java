package com.expensetracker.dashboard.listener;

import com.expensetracker.dashboard.graphql.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Map;
import java.math.BigDecimal;

@Service
public class IncomeEventListener {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "income-events", groupId = "dashboard-service-group", autoStartup = "true")
    public void handleIncomeEvent(String message) {
        System.out.println("IncomeEventListener received message: " + message);
        try {
            Map<String, Object> eventMap = objectMapper.readValue(message, Map.class);
            System.out.println("Parsed event map: " + eventMap);
            
            String userId = (String) eventMap.get("userId");
            Object amountObj = eventMap.get("amount");
            Double amount = (amountObj instanceof Number) ? ((Number) amountObj).doubleValue() : Double.parseDouble(amountObj.toString());
            String source = (String) eventMap.get("source");
            Object dateObj = eventMap.get("incomeDate");
            String dateStr = dateObj != null ? dateObj.toString() : (String) eventMap.get("date");
            
            if (dateStr == null) {
                System.err.println("Date field not found in event: " + eventMap.keySet());
                return;
            }

            // Extract month from date (YYYY-MM-DD format or ISO format)
            String month = dateStr.length() >= 7 ? dateStr.substring(0, 7) : dateStr;

            dashboardService.addIncome(userId, month, source, BigDecimal.valueOf(amount));
            System.out.println("✅ Income event processed for user: " + userId + ", amount: " + amount + ", month: " + month);
        } catch (Exception e) {
            System.err.println("❌ Error processing income event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
