package integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import com.pivotapp.interview.InterviewApplication;
import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.application.service.PurchaseRequestServiceImpl;
import com.pivotapp.interview.domain.model.PurchaseStatus;
import com.pivotapp.interview.domain.repository.PurchaseRequestRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {InterviewApplication.class})
@ActiveProfiles("test")
public class PurchaseRequestServiceIntegrationTest {

  @Autowired private PurchaseRequestServiceImpl service;

  @Autowired private PurchaseRequestRepository repository;

  @Test
  void testSubmitAndApprovePurchaseRequest() {
    InPurchaseRequest pr = new InPurchaseRequest("New Request", 1000.0, LocalDate.now());

    PurchaseRequestDto submitted = service.submitPurchaseRequest(pr);
    assertNotNull(submitted);
    assertEquals(PurchaseStatus.SUBMITTED, submitted.status());

    PurchaseRequestDto approvePurchaseRequest = service.processPurchaseRequest(submitted.id());
    assertNotNull(approvePurchaseRequest);
    assertEquals(PurchaseStatus.APPROVED, approvePurchaseRequest.status());
  }

  @Test
  void testSubmitAndApprovePurchaseRequest_unsufficientBudget() {
    InPurchaseRequest pr = new InPurchaseRequest("New Request", 500000.1, LocalDate.now());

    PurchaseRequestDto submitted = service.submitPurchaseRequest(pr);
    assertNotNull(submitted);
    assertEquals(PurchaseStatus.SUBMITTED, submitted.status());

    PurchaseRequestDto approvePurchaseRequest = service.processPurchaseRequest(submitted.id());
    assertNotNull(approvePurchaseRequest);
    assertEquals(PurchaseStatus.DECLINED, approvePurchaseRequest.status());
  }

  @Test
  void testSubmitAndDeclinePurchaseRequest() {
    InPurchaseRequest pr = new InPurchaseRequest("New Request", 1000.0, LocalDate.now());

    PurchaseRequestDto submitted = service.submitPurchaseRequest(pr);
    assertNotNull(submitted);
    assertEquals(PurchaseStatus.SUBMITTED, submitted.status());

    PurchaseRequestDto declinePurchaseRequest = service.declinePurchaseRequest(submitted.id());
    assertNotNull(declinePurchaseRequest);
    assertEquals(PurchaseStatus.DECLINED, declinePurchaseRequest.status());
  }
}
