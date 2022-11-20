package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkoutMapperTest {

    private WorkoutMapper workoutMapper;

    @BeforeEach
    public void setUp() {
        workoutMapper = new WorkoutMapperImpl();
    }
}
