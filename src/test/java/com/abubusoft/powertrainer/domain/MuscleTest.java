package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MuscleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Muscle.class);
        Muscle muscle1 = new Muscle();
        muscle1.setId(1L);
        Muscle muscle2 = new Muscle();
        muscle2.setId(muscle1.getId());
        assertThat(muscle1).isEqualTo(muscle2);
        muscle2.setId(2L);
        assertThat(muscle1).isNotEqualTo(muscle2);
        muscle1.setId(null);
        assertThat(muscle1).isNotEqualTo(muscle2);
    }
}
