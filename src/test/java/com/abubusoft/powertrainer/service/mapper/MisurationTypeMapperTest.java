package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MisurationTypeMapperTest {

    private MisurationTypeMapper misurationTypeMapper;

    @BeforeEach
    public void setUp() {
        misurationTypeMapper = new MisurationTypeMapperImpl();
    }
}
