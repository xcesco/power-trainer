package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutSheetExerciseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutSheetExerciseDTO.class);
        WorkoutSheetExerciseDTO workoutSheetExerciseDTO1 = new WorkoutSheetExerciseDTO();
        workoutSheetExerciseDTO1.setId(1L);
        WorkoutSheetExerciseDTO workoutSheetExerciseDTO2 = new WorkoutSheetExerciseDTO();
        assertThat(workoutSheetExerciseDTO1).isNotEqualTo(workoutSheetExerciseDTO2);
        workoutSheetExerciseDTO2.setId(workoutSheetExerciseDTO1.getId());
        assertThat(workoutSheetExerciseDTO1).isEqualTo(workoutSheetExerciseDTO2);
        workoutSheetExerciseDTO2.setId(2L);
        assertThat(workoutSheetExerciseDTO1).isNotEqualTo(workoutSheetExerciseDTO2);
        workoutSheetExerciseDTO1.setId(null);
        assertThat(workoutSheetExerciseDTO1).isNotEqualTo(workoutSheetExerciseDTO2);
    }
}
