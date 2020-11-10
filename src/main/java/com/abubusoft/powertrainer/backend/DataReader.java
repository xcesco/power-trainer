package com.abubusoft.powertrainer.backend;

import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.repositories.model.ExerciseResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataReader {
  public void read() throws IOException {
// Loading the YAML file from the /resources folder
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    File file = new File(classLoader.getResource("data/exercise_repository.yml").getFile());

// Instantiating a new ObjectMapper as a YAMLFactory
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.findAndRegisterModules();

// Mapping the employee from the YAML file to the Employee class
    List<ExerciseResource> exerciseResources = mapper.readValue(file, new TypeReference<>() {
    });


    exerciseResources.forEach(item -> {
      String fileName="data/" + item.getResource();
      File resourceFile = new File(classLoader.getResource(fileName).getFile());

      try {
        Exercise exercise = mapper.readValue(resourceFile, new TypeReference<>() {
        });

        System.out.println("Exercise " + exercise.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

// Printing out the information
    System.out.println("Employee info " + exerciseResources.toString());

// Access the first element of the list and print it as well
    //   System.out.println("Accessing first element: " + employee.getColleagues().get(0).toString());
  }
}
