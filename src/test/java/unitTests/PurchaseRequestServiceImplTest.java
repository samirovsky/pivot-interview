package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.pivotapp.interview.application.dto.BudgetDto;
import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.application.mapper.PurchaseRequestMapper;
import com.pivotapp.interview.application.service.PurchaseRequestServiceImpl;
import com.pivotapp.interview.domain.model.PurchaseRequest;
import com.pivotapp.interview.domain.model.PurchaseStatus;
import com.pivotapp.interview.domain.repository.PurchaseRequestRepository;
import com.pivotapp.interview.domain.service.BudgetService;
import com.pivotapp.interview.infrastructure.exceptions.PurchasingErrorException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PurchaseRequestServiceImplTest {

  @Mock private PurchaseRequestRepository purchaseRequestRepository;

  @Mock private BudgetService budgetService;

  @Mock private PurchaseRequestMapper mapper;

  @InjectMocks private PurchaseRequestServiceImpl service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSubmitPurchaseRequest() {
    // Given
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.SUBMITTED, "New Request", 1000.0, LocalDate.now(), null);
    when(mapper.toDto(any(PurchaseRequest.class))).thenReturn(purchaseRequestDto);

    PurchaseRequest pr = new PurchaseRequest();
    pr.setId(1L);
    pr.setStatus(PurchaseStatus.SUBMITTED);
    when(mapper.fromInDtoToDomain(any(InPurchaseRequest.class))).thenReturn(pr);

    when(purchaseRequestRepository.save(any(PurchaseRequest.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    InPurchaseRequest inPurchaseRequest =
        new InPurchaseRequest("New Request", 1000.0, LocalDate.now());

    // When
    PurchaseRequestDto savedPR = service.submitPurchaseRequest(inPurchaseRequest);

    // Then
    assertNotNull(savedPR);
    assertEquals(PurchaseStatus.SUBMITTED, savedPR.status());
  }

  @Test
  void testProcessPurchaseRequest_DECLINED() {
    // Given
    PurchaseRequest pr = new PurchaseRequest();
    pr.setId(1L);
    pr.setStatus(PurchaseStatus.SUBMITTED);
    pr.setAmount(1000.0);
    when(purchaseRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(pr));
    when(purchaseRequestRepository.save(any(PurchaseRequest.class)))
        .thenAnswer(i -> i.getArguments()[0]);
    when(budgetService.getLatestBudget()).thenReturn(500.0);
    when(budgetService.canPurchase(anyDouble(), anyDouble())).thenReturn(false);
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.DECLINED, "New Request", 1000.0, LocalDate.now(), null);
    when(mapper.toDto(any(PurchaseRequest.class))).thenReturn(purchaseRequestDto);

    // When
    PurchaseRequestDto approvedPR = service.processPurchaseRequest(1L);

    // Then
    assertNotNull(approvedPR);
    assertEquals(PurchaseStatus.DECLINED, approvedPR.status());
  }

  @Test
  void testProcessPurchaseRequest_APPROVED() {
    // Given
    PurchaseRequest pr = new PurchaseRequest();
    pr.setId(1L);
    pr.setStatus(PurchaseStatus.SUBMITTED);
    pr.setAmount(1000.0);
    when(purchaseRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(pr));
    when(purchaseRequestRepository.save(any(PurchaseRequest.class)))
        .thenAnswer(i -> i.getArguments()[0]);
    when(budgetService.getLatestBudget()).thenReturn(1500.0);
    when(budgetService.canPurchase(anyDouble(), anyDouble())).thenReturn(true);
    var budgetDto = new BudgetDto(500.0, 1000.0, LocalDateTime.now());
    when(budgetService.updateBudget(1000.0, 1500.0)).thenReturn(budgetDto);
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.APPROVED, "New Request", 1000.0, LocalDate.now(), budgetDto);
    when(mapper.toProcessedDto(any(PurchaseRequest.class), any(BudgetDto.class)))
        .thenReturn(purchaseRequestDto);

    // When
    PurchaseRequestDto approvedPR = service.processPurchaseRequest(1L);

    // Then
    assertNotNull(approvedPR);
    assertEquals(PurchaseStatus.APPROVED, approvedPR.status());
  }

  @Test
  void testDeclinePurchaseRequest() {
    // Given
    PurchaseRequest pr = new PurchaseRequest();
    pr.setId(1L);
    pr.setStatus(PurchaseStatus.SUBMITTED);
    when(purchaseRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(pr));
    when(purchaseRequestRepository.save(any(PurchaseRequest.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.DECLINED, "New Request", 1000.0, LocalDate.now(), null);
    when(mapper.toDto(any(PurchaseRequest.class))).thenReturn(purchaseRequestDto);

    // When
    PurchaseRequestDto approvedPR = service.declinePurchaseRequest(1L);

    // Then
    assertNotNull(approvedPR);
    assertEquals(PurchaseStatus.DECLINED, approvedPR.status());
  }

  @Test
  void approvePurchaseRequest_throwsException() {
    // Given
    when(purchaseRequestRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    Executable executable = () -> service.processPurchaseRequest(1L);

    // Then
    assertThrows(PurchasingErrorException.class, executable);
  }

  @Test
  void declinePurchaseRequest_throwsException() {
    // Given
    when(purchaseRequestRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    Executable executable = () -> service.declinePurchaseRequest(1L);

    // Then
    assertThrows(PurchasingErrorException.class, executable);
  }
}
