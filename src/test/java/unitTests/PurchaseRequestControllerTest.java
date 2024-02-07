package unitTests;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pivotapp.interview.application.dto.InPurchaseRequest;
import com.pivotapp.interview.application.dto.PurchaseRequestDto;
import com.pivotapp.interview.domain.model.PurchaseStatus;
import com.pivotapp.interview.domain.service.PurchaseRequestService;
import com.pivotapp.interview.infrastructure.web.PurchaseRequestController;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class PurchaseRequestControllerTest {

  private MockMvc mockMvc;

  @Mock private PurchaseRequestService purchaseRequestService;

  @InjectMocks private PurchaseRequestController controller;

  @BeforeEach
  public void setup() {
    openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void submitPurchaseRequest_ShouldReturnSubmittedRequest() throws Exception {
    // Given
    InPurchaseRequest inPurchaseRequest =
        new InPurchaseRequest("New Request", 1000.0, LocalDate.of(2024, 02, 07));
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.SUBMITTED, "New Request", 1000.0, LocalDate.of(2024, 02, 07), null);

    given(purchaseRequestService.submitPurchaseRequest(inPurchaseRequest))
        .willReturn(purchaseRequestDto);

    // When
    var resultsActions =
        mockMvc.perform(
            post("/purchase-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    "{\n"
                        + "  \"issue_date\": \"2024-02-07\",\n"
                        + "  \"description\": \"New Request\",\n"
                        + "  \"amount\": 1000.0\n"
                        + "}"));

    // Then
    resultsActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.issue_date", is("2024-02-07")))
        .andExpect(jsonPath("$.id", is(0)))
        .andExpect(jsonPath("$.status", is("SUBMITTED")))
        .andExpect(jsonPath("$.description", is("New Request")))
        .andExpect(jsonPath("$.amount", is(1000.0)));
  }

  @Test
  public void approvePurchaseRequest_ShouldReturnApprovedRequest() throws Exception {
    // Given
    Long requestId = 1L; // Example request ID
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.APPROVED, "New Request", 1000.0, LocalDate.of(2024, 02, 07), null);

    given(purchaseRequestService.processPurchaseRequest(requestId)).willReturn(purchaseRequestDto);

    // When
    var resultsActions = mockMvc.perform(put("/purchase-requests/{id}/approve", requestId));

    // Then
    resultsActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.issue_date", is("2024-02-07")))
        .andExpect(jsonPath("$.id", is(0)))
        .andExpect(jsonPath("$.status", is("APPROVED")))
        .andExpect(jsonPath("$.description", is("New Request")))
        .andExpect(jsonPath("$.amount", is(1000.0)));
  }

  @Test
  public void declinePurchaseRequest_ShouldReturnDeclinedRequest() throws Exception {
    Long requestId = 1L; // Example request ID
    PurchaseRequestDto purchaseRequestDto =
        new PurchaseRequestDto(
            0L, PurchaseStatus.DECLINED, "New Request", 1000.0, LocalDate.of(2024, 02, 07), null);

    given(purchaseRequestService.declinePurchaseRequest(requestId)).willReturn(purchaseRequestDto);

    var resultsActions = mockMvc.perform(put("/purchase-requests/{id}/decline", requestId));

    // Then
    resultsActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.issue_date", is("2024-02-07")))
        .andExpect(jsonPath("$.id", is(0)))
        .andExpect(jsonPath("$.status", is("DECLINED")))
        .andExpect(jsonPath("$.description", is("New Request")))
        .andExpect(jsonPath("$.amount", is(1000.0)));
  }
}
