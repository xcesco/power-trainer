package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExerciseValueMapperTest {

    private ExerciseValueMapper exerciseValueMapper;

    @BeforeEach
    public void setUp() {
        exerciseValueMapper = new ExerciseValueMapperImpl();
    }
}
