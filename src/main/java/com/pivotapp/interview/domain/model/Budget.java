package com.pivotapp.interview.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "BUDGET")
public class Budget {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double totalBudget;
  private Double currentExpenditure;

  @Column(name = "last_updated_date")
  private LocalDateTime lastUpdatedDate;

  public Budget() {
    this.lastUpdatedDate = LocalDateTime.now();
  }
}
