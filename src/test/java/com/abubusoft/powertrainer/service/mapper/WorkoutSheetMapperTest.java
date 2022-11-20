package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkoutSheetMapperTest {

    private WorkoutSheetMapper workoutSheetMapper;

    @BeforeEach
    public void setUp() {
        workoutSheetMapper = new WorkoutSheetMapperImpl();
    }
}
