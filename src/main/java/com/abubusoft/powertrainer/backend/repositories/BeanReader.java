package com.abubusoft.powertrainer.backend.repositories;

import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.repositories.model.ExerciseResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BeanReader implements ResourceReader {
  public static final String EXERCISE_REPOSITORY_YML = "data/exercise_repository.yml";
  private static final String DATA_PATH = "data/";
  protected static final String DEFAULT_TYPE_KEY = "_class";
  protected static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper(new YAMLFactory());
  protected final ObjectMapper mapper;
  private String typeKey;

  public BeanReader(Class<?> resourceClazz) {
    this(DEFAULT_MAPPER, resourceClazz);
  }

  public BeanReader(@Nullable ObjectMapper mapper, Class<?> resourceClazz) {
    this.typeKey = resourceClazz.getTypeName();
    this.mapper = mapper == null ? DEFAULT_MAPPER : mapper;
  }

  @Override
  public Object readFrom(Resource resource, ClassLoader classLoader) throws Exception {
    final List<Exercise> result = new ArrayList<>();
    File file = resource.getFile();

    //mapper.findAndRegisterModules();
    List<ExerciseResource> exerciseResources = mapper.readValue(file, new TypeReference<>() {
    });

    exerciseResources.forEach(item -> {
      result.add(readSingle(item, classLoader));
    });

    System.out.println("Employee info " + exerciseResources.toString());

    return result;
  }

  protected Exercise readSingle(ExerciseResource item, @Nullable ClassLoader classLoader) {
    String fileName = DATA_PATH + item.getResource();

    try {
      File resourceFile = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());

      return mapper.readValue(resourceFile, new TypeReference<>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
