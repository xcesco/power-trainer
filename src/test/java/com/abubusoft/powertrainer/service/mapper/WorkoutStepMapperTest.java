package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkoutStepMapperTest {

    private WorkoutStepMapper workoutStepMapper;

    @BeforeEach
    public void setUp() {
        workoutStepMapper = new WorkoutStepMapperImpl();
    }
}
