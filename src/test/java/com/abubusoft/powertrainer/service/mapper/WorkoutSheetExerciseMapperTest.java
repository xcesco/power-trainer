package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkoutSheetExerciseMapperTest {

    private WorkoutSheetExerciseMapper workoutSheetExerciseMapper;

    @BeforeEach
    public void setUp() {
        workoutSheetExerciseMapper = new WorkoutSheetExerciseMapperImpl();
    }
}
