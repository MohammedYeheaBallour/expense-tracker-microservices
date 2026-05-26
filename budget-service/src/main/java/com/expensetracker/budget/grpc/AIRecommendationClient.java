package com.expensetracker.budget.grpc;

import com.expensetracker.grpc.AIRecommendationGrpc;
import com.expensetracker.grpc.RecommendationRequest;
import com.expensetracker.grpc.RecommendationResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AIRecommendationClient {

    private AIRecommendationGrpc.AIRecommendationBlockingStub aiRecommendationStub;
    private ManagedChannel channel;

    private void init() {
        if (aiRecommendationStub == null) {
            try {
                channel = ManagedChannelBuilder.forAddress("localhost", 9005)
                        .usePlaintext()
                        .build();
                this.aiRecommendationStub = AIRecommendationGrpc.newBlockingStub(channel);
            } catch (Exception e) {
                System.err.println("Failed to initialize gRPC client: " + e.getMessage());
            }
        }
    }

    public RecommendationResponse getRecommendation(String userId, double monthlyBudget, double currentExpenses) {
        init();
        if (aiRecommendationStub == null) {
            throw new RuntimeException("gRPC client not initialized");
        }
        RecommendationRequest request = RecommendationRequest.newBuilder()
                .setUserId(userId)
                .setMonthlyBudget(monthlyBudget)
                .setCurrentExpenses(currentExpenses)
                .build();

        return aiRecommendationStub.getRecommendation(request);
    }

}
