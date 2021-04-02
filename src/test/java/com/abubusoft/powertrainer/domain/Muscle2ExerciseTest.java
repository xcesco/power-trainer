package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Muscle2ExerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Muscle2Exercise.class);
        Muscle2Exercise muscle2Exercise1 = new Muscle2Exercise();
        muscle2Exercise1.setId(1L);
        Muscle2Exercise muscle2Exercise2 = new Muscle2Exercise();
        muscle2Exercise2.setId(muscle2Exercise1.getId());
        assertThat(muscle2Exercise1).isEqualTo(muscle2Exercise2);
        muscle2Exercise2.setId(2L);
        assertThat(muscle2Exercise1).isNotEqualTo(muscle2Exercise2);
        muscle2Exercise1.setId(null);
        assertThat(muscle2Exercise1).isNotEqualTo(muscle2Exercise2);
    }
}
