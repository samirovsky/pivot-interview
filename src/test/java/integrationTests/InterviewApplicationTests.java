package integrationTests;

import com.pivotapp.interview.InterviewApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = InterviewApplication.class)
class InterviewApplicationTests {
  @Test
  void contextLoads() {}
}
