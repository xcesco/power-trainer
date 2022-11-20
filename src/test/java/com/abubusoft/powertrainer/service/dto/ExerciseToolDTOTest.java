package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseToolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseToolDTO.class);
        ExerciseToolDTO exerciseToolDTO1 = new ExerciseToolDTO();
        exerciseToolDTO1.setId(1L);
        ExerciseToolDTO exerciseToolDTO2 = new ExerciseToolDTO();
        assertThat(exerciseToolDTO1).isNotEqualTo(exerciseToolDTO2);
        exerciseToolDTO2.setId(exerciseToolDTO1.getId());
        assertThat(exerciseToolDTO1).isEqualTo(exerciseToolDTO2);
        exerciseToolDTO2.setId(2L);
        assertThat(exerciseToolDTO1).isNotEqualTo(exerciseToolDTO2);
        exerciseToolDTO1.setId(null);
        assertThat(exerciseToolDTO1).isNotEqualTo(exerciseToolDTO2);
    }
}
