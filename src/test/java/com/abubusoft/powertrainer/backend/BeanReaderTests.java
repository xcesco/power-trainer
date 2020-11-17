package com.abubusoft.powertrainer.backend;

import com.abubusoft.powertrainer.backend.repositories.populators.BeanReader;
import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
class BeanReaderTests {

  @Test
  void contextLoads() throws Exception {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    BeanReader reader = new BeanReader(Exercise.class);
    reader.readFrom(new ClassPathResource(BeanReader.EXERCISE_REPOSITORY_YML), classLoader);
  }

}
