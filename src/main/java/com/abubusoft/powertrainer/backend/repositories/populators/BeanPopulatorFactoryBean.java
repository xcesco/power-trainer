package com.abubusoft.powertrainer.backend.repositories.populators;

import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.ResourceReader;


public class BeanPopulatorFactoryBean extends AbstractRepositoryPopulatorFactoryBean {

    protected ResourceReader getResourceReader() {
        return new Bean2ResourceReader();
    }
}