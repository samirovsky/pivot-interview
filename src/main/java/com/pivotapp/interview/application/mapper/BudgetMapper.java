package com.pivotapp.interview.application.mapper;

import com.pivotapp.interview.application.dto.BudgetDto;
import com.pivotapp.interview.domain.model.Budget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

  BudgetDto toDto(Budget budget);
}
