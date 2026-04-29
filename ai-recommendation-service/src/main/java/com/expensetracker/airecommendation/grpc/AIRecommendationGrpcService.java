package com.expensetracker.airecommendation.grpc;

import com.expensetracker.grpc.AIRecommendationGrpc;
import com.expensetracker.grpc.RecommendationRequest;
import com.expensetracker.grpc.RecommendationResponse;
import com.expensetracker.airecommendation.service.RecommendationService;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class AIRecommendationGrpcService extends AIRecommendationGrpc.AIRecommendationImplBase {

    @Autowired
    private RecommendationService recommendationService;

    @Override
    public void getRecommendation(RecommendationRequest request,
                                   StreamObserver<RecommendationResponse> responseObserver) {
        String userId = request.getUserId();
        double monthlyBudget = request.getMonthlyBudget();
        double currentExpenses = request.getCurrentExpenses();

        String advice = recommendationService.generateAdvice(monthlyBudget, currentExpenses);
        double spendingPercentage = recommendationService.calculateSpendingPercentage(monthlyBudget, currentExpenses);
        boolean isWithinBudget = recommendationService.isWithinBudget(monthlyBudget, currentExpenses);

        RecommendationResponse response = RecommendationResponse.newBuilder()
                .setUserId(userId)
                .setAdvice(advice)
                .setSpendingPercentage(spendingPercentage)
                .setIsWithinBudget(isWithinBudget)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
