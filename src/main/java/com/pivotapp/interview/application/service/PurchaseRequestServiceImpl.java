package com.pivotapp.interview.application.service;

import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.application.mapper.PurchaseRequestMapper;
import com.pivotapp.interview.domain.model.PurchaseStatus;
import com.pivotapp.interview.domain.repository.PurchaseRequestRepository;
import com.pivotapp.interview.domain.service.BudgetService;
import com.pivotapp.interview.domain.service.PurchaseRequestService;
import com.pivotapp.interview.infrastructure.exceptions.PurchasingErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

  private static final double WARNING_PURCHASE_AMOUNT = 50000;

  private final PurchaseRequestRepository repository;

  private final BudgetService budgetService;

  private final PurchaseRequestMapper mapper;

  @Autowired
  public PurchaseRequestServiceImpl(
      PurchaseRequestRepository repository,
      BudgetService budgetService,
      PurchaseRequestMapper mapper) {
    this.repository = repository;
    this.budgetService = budgetService;
    this.mapper = mapper;
  }

  public PurchaseRequestDto submitPurchaseRequest(InPurchaseRequest inPurchaseRequest) {
    if (inPurchaseRequest.amount() > WARNING_PURCHASE_AMOUNT) {
      log.warn("Purchase request has amount more than {}", WARNING_PURCHASE_AMOUNT);
    }
    var pr = mapper.fromInDtoToDomain(inPurchaseRequest);
    pr.setStatus(PurchaseStatus.SUBMITTED);
    return mapper.toDto(repository.save(pr));
  }

  @Override
  public PurchaseRequestDto processPurchaseRequest(Long id) {
    var purchaseRequest =
        repository
            .findById(id)
            .orElseThrow(
                () -> new PurchasingErrorException("PurchaseRequest not found with id " + id));
    var lastBudget = budgetService.getLatestBudget();
    if (!budgetService.canPurchase(purchaseRequest.getAmount(), lastBudget)) {
      log.warn(
          "Declined Purchase request. Insufficient current budget = %s for Purchase Amount: %s."
              .formatted(lastBudget, purchaseRequest.getAmount()));
      purchaseRequest.setStatus(PurchaseStatus.DECLINED);
      return mapper.toDto(repository.save(purchaseRequest));
    } else {
      purchaseRequest.setStatus(PurchaseStatus.APPROVED);
      var budget = budgetService.updateBudget(purchaseRequest.getAmount(), lastBudget);
      return mapper.toProcessedDto(repository.save(purchaseRequest), budget);
    }
  }

  public PurchaseRequestDto declinePurchaseRequest(Long id) {
    return repository
        .findById(id)
        .map(
            pr -> {
              pr.setStatus(PurchaseStatus.DECLINED);
              return mapper.toDto(repository.save(pr));
            })
        .orElseThrow(() -> new PurchasingErrorException("PurchaseRequest not found with id " + id));
  }
}
