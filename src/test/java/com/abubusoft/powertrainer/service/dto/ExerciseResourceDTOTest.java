package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseResourceDTO.class);
        ExerciseResourceDTO exerciseResourceDTO1 = new ExerciseResourceDTO();
        exerciseResourceDTO1.setId(1L);
        ExerciseResourceDTO exerciseResourceDTO2 = new ExerciseResourceDTO();
        assertThat(exerciseResourceDTO1).isNotEqualTo(exerciseResourceDTO2);
        exerciseResourceDTO2.setId(exerciseResourceDTO1.getId());
        assertThat(exerciseResourceDTO1).isEqualTo(exerciseResourceDTO2);
        exerciseResourceDTO2.setId(2L);
        assertThat(exerciseResourceDTO1).isNotEqualTo(exerciseResourceDTO2);
        exerciseResourceDTO1.setId(null);
        assertThat(exerciseResourceDTO1).isNotEqualTo(exerciseResourceDTO2);
    }
}
