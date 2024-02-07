package com.pivotapp.interview.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pivotapp.interview.domain.model.PurchaseStatus;
import java.time.LocalDate;

public record PurchaseRequestDto(
    Long id,
    PurchaseStatus status,
    String description,
    Double amount,
    @JsonProperty("issue_date") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate issueDate,
    @JsonProperty("current_budget") BudgetDto currentBudget) {}
