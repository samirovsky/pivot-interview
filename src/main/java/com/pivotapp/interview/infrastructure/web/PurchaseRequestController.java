package com.pivotapp.interview.infrastructure.web;

import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.domain.service.PurchaseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase-requests")
public class PurchaseRequestController {

  private final PurchaseRequestService purchaseRequestService;

  @Autowired
  public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
    this.purchaseRequestService = purchaseRequestService;
  }

  @PostMapping
  public ResponseEntity<PurchaseRequestDto> submitPurchaseRequest(
      @RequestBody InPurchaseRequest purchaseRequest) {
    PurchaseRequestDto submittedRequest =
        purchaseRequestService.submitPurchaseRequest(purchaseRequest);
    return ResponseEntity.ok(submittedRequest);
  }

  @PutMapping("/{id}/approve")
  public ResponseEntity<PurchaseRequestDto> approvePurchaseRequest(@PathVariable Long id) {
    PurchaseRequestDto approvedRequest = purchaseRequestService.processPurchaseRequest(id);
    return ResponseEntity.ok(approvedRequest);
  }

  @PutMapping("/{id}/decline")
  public ResponseEntity<PurchaseRequestDto> declinePurchaseRequest(@PathVariable Long id) {
    PurchaseRequestDto declinedRequest = purchaseRequestService.declinePurchaseRequest(id);
    return ResponseEntity.ok(declinedRequest);
  }
}
