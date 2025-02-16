package com.velora;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CodeApplication.class)
@ActiveProfiles("test")
class CodeApplicationTests {

  @Test
  void contextLoads() {
  }

}
