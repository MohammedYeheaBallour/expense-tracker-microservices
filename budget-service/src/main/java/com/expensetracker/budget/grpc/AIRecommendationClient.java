package com.expensetracker.budget.grpc;

import com.expensetracker.grpc.AIRecommendationGrpc;
import com.expensetracker.grpc.RecommendationRequest;
import com.expensetracker.grpc.RecommendationResponse;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class AIRecommendationClient {

    @GrpcClient("aiRecommendationService")
    private AIRecommendationGrpc.AIRecommendationBlockingStub aiRecommendationStub;

    public RecommendationResponse getRecommendation(String userId, double monthlyBudget, double currentExpenses) {
        RecommendationRequest request = RecommendationRequest.newBuilder()
                .setUserId(userId)
                .setMonthlyBudget(monthlyBudget)
                .setCurrentExpenses(currentExpenses)
                .build();

        return aiRecommendationStub.getRecommendation(request);
    }

}
