package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExerciseResourceMapperTest {

    private ExerciseResourceMapper exerciseResourceMapper;

    @BeforeEach
    public void setUp() {
        exerciseResourceMapper = new ExerciseResourceMapperImpl();
    }
}
