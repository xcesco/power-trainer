package com.abubusoft.powertrainer.backend.repositories;

import com.abubusoft.powertrainer.backend.repositories.populators.BeanPopulatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class RepositoriesConfiguration {

  public static final String DATA_EXERCISE_REPOSITORY = "data/exercise_repository.yml";

  @Bean
  public BeanPopulatorFactoryBean getRespositoryPopulator() {
    BeanPopulatorFactoryBean factory = new BeanPopulatorFactoryBean();
    factory.setResources(new Resource[]{new ClassPathResource(DATA_EXERCISE_REPOSITORY)});
    return factory;
  }

}