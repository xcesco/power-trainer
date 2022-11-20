package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MisurationMapperTest {

    private MisurationMapper misurationMapper;

    @BeforeEach
    public void setUp() {
        misurationMapper = new MisurationMapperImpl();
    }
}
