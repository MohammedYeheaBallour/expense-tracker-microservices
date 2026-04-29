package com.expensetracker.income.service;

import com.expensetracker.income.entity.Income;
import com.expensetracker.income.event.IncomeEvent;
import com.expensetracker.income.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired(required = false)
    private KafkaTemplate<String, IncomeEvent> kafkaTemplate;

    public Income createIncome(Income income) {
        Income savedIncome = incomeRepository.save(income);

        // Publish event to Kafka
        if (kafkaTemplate != null) {
            IncomeEvent event = new IncomeEvent(
                    savedIncome.getId(),
                    savedIncome.getUserId(),
                    savedIncome.getAmount(),
                    savedIncome.getSource(),
                    savedIncome.getDate(),
                    LocalDateTime.now()
            );
            kafkaTemplate.send("income-events", event.getUserId(), event);
        }

        return savedIncome;
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Optional<Income> getIncomeById(Long id) {
        return incomeRepository.findById(id);
    }

    public List<Income> getIncomesByUserId(String userId) {
        return incomeRepository.findByUserId(userId);
    }

    public Income updateIncome(Long id, Income incomeDetails) {
        return incomeRepository.findById(id)
                .map(income -> {
                    income.setAmount(incomeDetails.getAmount());
                    income.setSource(incomeDetails.getSource());
                    income.setDate(incomeDetails.getDate());
                    return incomeRepository.save(income);
                })
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

}
