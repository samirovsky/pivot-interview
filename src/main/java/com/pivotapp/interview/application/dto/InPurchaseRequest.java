package com.pivotapp.interview.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record InPurchaseRequest(
    String description,
    Double amount,
    @JsonProperty("issue_date") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate issueDate) {}
