package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExerciseMapperTest {

    private ExerciseMapper exerciseMapper;

    @BeforeEach
    public void setUp() {
        exerciseMapper = new ExerciseMapperImpl();
    }
}
