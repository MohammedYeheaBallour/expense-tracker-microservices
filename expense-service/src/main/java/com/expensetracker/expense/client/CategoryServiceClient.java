package com.expensetracker.expense.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CategoryServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CATEGORY_SERVICE_URL = "http://localhost:8003/api/categories";

    public boolean validateCategory(String categoryName) {
        try {
            String url = CATEGORY_SERVICE_URL + "/validate/" + categoryName;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("isValid")) {
                return (boolean) response.get("isValid");
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error validating category: " + e.getMessage());
            return false;
        }
    }

}
