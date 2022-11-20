package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseValueDTO.class);
        ExerciseValueDTO exerciseValueDTO1 = new ExerciseValueDTO();
        exerciseValueDTO1.setId(1L);
        ExerciseValueDTO exerciseValueDTO2 = new ExerciseValueDTO();
        assertThat(exerciseValueDTO1).isNotEqualTo(exerciseValueDTO2);
        exerciseValueDTO2.setId(exerciseValueDTO1.getId());
        assertThat(exerciseValueDTO1).isEqualTo(exerciseValueDTO2);
        exerciseValueDTO2.setId(2L);
        assertThat(exerciseValueDTO1).isNotEqualTo(exerciseValueDTO2);
        exerciseValueDTO1.setId(null);
        assertThat(exerciseValueDTO1).isNotEqualTo(exerciseValueDTO2);
    }
}
