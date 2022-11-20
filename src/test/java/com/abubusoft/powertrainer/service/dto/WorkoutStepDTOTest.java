package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutStepDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutStepDTO.class);
        WorkoutStepDTO workoutStepDTO1 = new WorkoutStepDTO();
        workoutStepDTO1.setId(1L);
        WorkoutStepDTO workoutStepDTO2 = new WorkoutStepDTO();
        assertThat(workoutStepDTO1).isNotEqualTo(workoutStepDTO2);
        workoutStepDTO2.setId(workoutStepDTO1.getId());
        assertThat(workoutStepDTO1).isEqualTo(workoutStepDTO2);
        workoutStepDTO2.setId(2L);
        assertThat(workoutStepDTO1).isNotEqualTo(workoutStepDTO2);
        workoutStepDTO1.setId(null);
        assertThat(workoutStepDTO1).isNotEqualTo(workoutStepDTO2);
    }
}
