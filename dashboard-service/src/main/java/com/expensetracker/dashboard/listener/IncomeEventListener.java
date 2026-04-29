package com.expensetracker.dashboard.listener;

import com.expensetracker.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Map;

@Service
public class IncomeEventListener {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "income-events", groupId = "dashboard-service-group")
    public void handleIncomeEvent(String message) {
        try {
            Map<String, Object> eventMap = objectMapper.readValue(message, Map.class);
            String userId = (String) eventMap.get("userId");
            Double amount = ((Number) eventMap.get("amount")).doubleValue();
            String source = (String) eventMap.get("source");
            String dateStr = (String) eventMap.get("incomeDate");

            // Extract month from date (YYYY-MM-DD format)
            String month = dateStr.substring(0, 7);

            dashboardService.addIncome(userId, month, source, java.math.BigDecimal.valueOf(amount));
            System.out.println("Income event processed for user: " + userId + ", amount: " + amount);
        } catch (Exception e) {
            System.err.println("Error processing income event: " + e.getMessage());
        }
    }

}
