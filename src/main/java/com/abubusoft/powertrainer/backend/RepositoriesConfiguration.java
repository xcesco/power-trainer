package com.abubusoft.powertrainer.backend;

import com.abubusoft.powertrainer.backend.repositories.BeanPopulatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class RepositoriesConfiguration {
  @Bean
  public BeanPopulatorFactoryBean getRespositoryPopulator() {
    BeanPopulatorFactoryBean factory = new BeanPopulatorFactoryBean();
    factory.setResources(new Resource[]{new ClassPathResource("data/exercise_repository.yml")});
    return factory;
  }
}