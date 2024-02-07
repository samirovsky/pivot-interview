package com.pivotapp.interview.domain.service;

import com.pivotapp.interview.application.dto.BudgetDto;

public interface BudgetService {

  BudgetDto updateBudget(double purchaseAmount, double lastBudget);

  double getLatestBudget();

  boolean canPurchase(double purchaseAmount, double lastBudget);
}
