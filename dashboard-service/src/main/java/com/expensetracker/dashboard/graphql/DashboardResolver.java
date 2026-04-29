package com.expensetracker.dashboard.graphql;

import com.expensetracker.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardResolver {

    @Autowired
    private DashboardService dashboardService;

    @QueryMapping
    public DashboardDTO financialOverview(@Argument String userId, @Argument String month) {
        return dashboardService.getFinancialOverview(userId, month);
    }

    @QueryMapping
    public SummaryDTO summary(@Argument String userId) {
        return dashboardService.getSummary(userId);
    }

}
