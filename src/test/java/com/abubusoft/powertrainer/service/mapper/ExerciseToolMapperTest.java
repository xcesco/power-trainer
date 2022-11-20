package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExerciseToolMapperTest {

    private ExerciseToolMapper exerciseToolMapper;

    @BeforeEach
    public void setUp() {
        exerciseToolMapper = new ExerciseToolMapperImpl();
    }
}
