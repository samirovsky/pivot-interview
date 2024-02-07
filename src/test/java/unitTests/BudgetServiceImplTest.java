package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pivotapp.interview.application.dto.BudgetDto;
import com.pivotapp.interview.application.mapper.BudgetMapper;
import com.pivotapp.interview.application.service.BudgetServiceImpl;
import com.pivotapp.interview.domain.model.Budget;
import com.pivotapp.interview.domain.repository.BudgetRepository;
import com.pivotapp.interview.infrastructure.exceptions.PurchasingErrorException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

public class BudgetServiceImplTest {

  @Mock private BudgetRepository budgetService;

  @Mock private BudgetMapper budgetMapper;

  @InjectMocks private BudgetServiceImpl service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUpdateBudget() {
    // Given
    final BudgetDto expectedResult =
        new BudgetDto(500.0, 1000.0, LocalDateTime.of(2020, 1, 1, 0, 0, 0));

    final Budget budget = new Budget();
    budget.setId(0L);
    budget.setTotalBudget(1500.0);
    budget.setCurrentExpenditure(100.0);
    budget.setLastUpdatedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    when(budgetService.save(any(Budget.class))).thenReturn(budget);

    final BudgetDto budgetDto = new BudgetDto(500.0, 1000.0, LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    when(budgetMapper.toDto(any(Budget.class))).thenReturn(budgetDto);

    // When
    final BudgetDto result = service.updateBudget(1000.0, 1500.0);

    // Then
    assertEquals(expectedResult, result);
  }

  @Test
  void testUpdateBudget_Exception() {
    // When
    Executable result = () -> service.updateBudget(1000, 500);

    // Then
    assertThrows(PurchasingErrorException.class, result);
  }

  @Test
  void testGetLatestBudget() {
    // Given
    final Budget budget1 = new Budget();
    budget1.setId(0L);
    budget1.setTotalBudget(1200.0);
    budget1.setCurrentExpenditure(300.0);
    budget1.setLastUpdatedDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    final Budget budget2 = new Budget();
    budget2.setId(2L);
    budget2.setTotalBudget(1500.0);
    budget2.setCurrentExpenditure(1000.0);
    budget2.setLastUpdatedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    final List<Budget> budgets = List.of(budget1, budget2);
    when(budgetService.findAll(Sort.by(Sort.Direction.DESC, "lastUpdatedDate")))
        .thenReturn(budgets);

    // When
    final double result = service.getLatestBudget();

    // Then
    assertEquals(1200.0, result, 0.0001);
  }
}
