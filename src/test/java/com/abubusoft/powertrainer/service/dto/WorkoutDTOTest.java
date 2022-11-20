package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutDTO.class);
        WorkoutDTO workoutDTO1 = new WorkoutDTO();
        workoutDTO1.setId(1L);
        WorkoutDTO workoutDTO2 = new WorkoutDTO();
        assertThat(workoutDTO1).isNotEqualTo(workoutDTO2);
        workoutDTO2.setId(workoutDTO1.getId());
        assertThat(workoutDTO1).isEqualTo(workoutDTO2);
        workoutDTO2.setId(2L);
        assertThat(workoutDTO1).isNotEqualTo(workoutDTO2);
        workoutDTO1.setId(null);
        assertThat(workoutDTO1).isNotEqualTo(workoutDTO2);
    }
}
