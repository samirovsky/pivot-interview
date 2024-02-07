package com.pivotapp.interview.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record BudgetDto(
    Double totalBudget,
    Double currentExpenditure,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime lastUpdatedDate) {}
