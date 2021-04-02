package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutSheetExerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutSheetExercise.class);
        WorkoutSheetExercise workoutSheetExercise1 = new WorkoutSheetExercise();
        workoutSheetExercise1.setId(1L);
        WorkoutSheetExercise workoutSheetExercise2 = new WorkoutSheetExercise();
        workoutSheetExercise2.setId(workoutSheetExercise1.getId());
        assertThat(workoutSheetExercise1).isEqualTo(workoutSheetExercise2);
        workoutSheetExercise2.setId(2L);
        assertThat(workoutSheetExercise1).isNotEqualTo(workoutSheetExercise2);
        workoutSheetExercise1.setId(null);
        assertThat(workoutSheetExercise1).isNotEqualTo(workoutSheetExercise2);
    }
}
