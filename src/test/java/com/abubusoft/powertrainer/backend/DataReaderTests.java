package com.abubusoft.powertrainer.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DataReaderTests {

  @Test
  void contextLoads() throws IOException {
    DataReader reader = new DataReader();
    reader.read();
  }

}
