package com.pivotapp.interview.application.mapper;

import com.pivotapp.interview.application.dto.BudgetDto;
import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.domain.model.PurchaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper {

  @Mapping(target = "currentBudget", ignore = true)
  PurchaseRequestDto toDto(PurchaseRequest purchaseRequest);

  @Mapping(source = "budget", target = "currentBudget")
  @Mapping(source = "purchaseRequest.status", target = "status")
  @Mapping(source = "purchaseRequest.description", target = "description")
  @Mapping(source = "purchaseRequest.amount", target = "amount")
  @Mapping(source = "purchaseRequest.issueDate", target = "issueDate")
  PurchaseRequestDto toProcessedDto(PurchaseRequest purchaseRequest, BudgetDto budget);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  PurchaseRequest fromInDtoToDomain(InPurchaseRequest inPurchaseRequest);
}
