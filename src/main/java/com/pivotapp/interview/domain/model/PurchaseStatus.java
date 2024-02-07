package com.pivotapp.interview.domain.model;

import lombok.Getter;

@Getter
public enum PurchaseStatus {
  CREATED("Created"),
  SUBMITTED("Submitted"),
  APPROVED("Approved"),
  DECLINED("Declined");

  private final String displayName;

  PurchaseStatus(String displayName) {
    this.displayName = displayName;
  }
}
