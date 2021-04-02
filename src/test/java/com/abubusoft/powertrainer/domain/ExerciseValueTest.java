package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseValue.class);
        ExerciseValue exerciseValue1 = new ExerciseValue();
        exerciseValue1.setId(1L);
        ExerciseValue exerciseValue2 = new ExerciseValue();
        exerciseValue2.setId(exerciseValue1.getId());
        assertThat(exerciseValue1).isEqualTo(exerciseValue2);
        exerciseValue2.setId(2L);
        assertThat(exerciseValue1).isNotEqualTo(exerciseValue2);
        exerciseValue1.setId(null);
        assertThat(exerciseValue1).isNotEqualTo(exerciseValue2);
    }
}
