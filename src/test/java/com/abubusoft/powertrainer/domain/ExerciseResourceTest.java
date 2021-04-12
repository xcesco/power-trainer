package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseResource.class);
        ExerciseResource exerciseResource1 = new ExerciseResource();
        exerciseResource1.setId(1L);
        ExerciseResource exerciseResource2 = new ExerciseResource();
        exerciseResource2.setId(exerciseResource1.getId());
        assertThat(exerciseResource1).isEqualTo(exerciseResource2);
        exerciseResource2.setId(2L);
        assertThat(exerciseResource1).isNotEqualTo(exerciseResource2);
        exerciseResource1.setId(null);
        assertThat(exerciseResource1).isNotEqualTo(exerciseResource2);
    }
}
