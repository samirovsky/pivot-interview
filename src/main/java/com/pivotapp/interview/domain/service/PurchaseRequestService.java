package com.pivotapp.interview.domain.service;

import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;

public interface PurchaseRequestService {
  PurchaseRequestDto submitPurchaseRequest(InPurchaseRequest inPurchaseRequest);

  PurchaseRequestDto processPurchaseRequest(Long id);

  PurchaseRequestDto declinePurchaseRequest(Long id);
}
