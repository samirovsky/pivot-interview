package com.pivotapp.interview.application.service;

import com.pivotapp.interview.application.dto.BudgetDto;
import com.pivotapp.interview.application.mapper.BudgetMapper;
import com.pivotapp.interview.domain.model.Budget;
import com.pivotapp.interview.domain.repository.BudgetRepository;
import com.pivotapp.interview.domain.service.BudgetService;
import com.pivotapp.interview.infrastructure.exceptions.PurchasingErrorException;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {
  private static final double INITIAL_BUDGET = 500000.0;
  private final BudgetRepository budgetRepository;

  private final BudgetMapper mapper;

  @Autowired
  public BudgetServiceImpl(BudgetRepository budgetRepository, BudgetMapper mapper) {
    this.budgetRepository = budgetRepository;
    this.mapper = mapper;
  }

  @Override
  public BudgetDto updateBudget(double purchaseAmount, double lastBudget) {
    if (!canPurchase(purchaseAmount, lastBudget)) {
      throw new PurchasingErrorException(
          "Insufficient budget. Details: Current Budget= %s. Purchase Amount: %s."
              .formatted(lastBudget, purchaseAmount));
    }
    var budget = new Budget();
    budget.setTotalBudget(lastBudget - purchaseAmount);
    budget.setCurrentExpenditure(purchaseAmount);
    budget.setLastUpdatedDate(LocalDateTime.now());
    return mapper.toDto(budgetRepository.save(budget));
  }

  @Override
  public double getLatestBudget() {
    return budgetRepository
        .findAll(Sort.by(Sort.Direction.DESC, "lastUpdatedDate"))
        .getFirst()
        .getTotalBudget();
  }

  @Override
  public boolean canPurchase(double purchaseAmount, double lastBudget) {
    return lastBudget >= purchaseAmount && purchaseAmount > 0;
  }

  @PostConstruct
  void initBudget() {
    var budget = new Budget();
    budget.setTotalBudget(INITIAL_BUDGET);
    budgetRepository.save(budget);
  }
}
