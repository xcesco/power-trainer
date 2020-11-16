package com.abubusoft.powertrainer.backend.repositories;

import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.ResourceReader;

import java.util.Collections;

public class Bean2ResourceReader implements ResourceReader {

  private static final Logger logger = LoggerFactory.getLogger(Bean2ResourceReader.class);

  private final BeanReader resourceReader = new BeanReader(Exercise.class);

  @Override
  public Object readFrom(Resource resource, ClassLoader nullClassLoader) throws Exception {
    Object result;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try {
      result = resourceReader.readFrom(resource, classLoader);
    } catch (Exception e) {
      logger.warn("Can't read from resource", e);
      return Collections.EMPTY_LIST;
    }
    return result;
  }

}