package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workout.class);
        Workout workout1 = new Workout();
        workout1.setId(1L);
        Workout workout2 = new Workout();
        workout2.setId(workout1.getId());
        assertThat(workout1).isEqualTo(workout2);
        workout2.setId(2L);
        assertThat(workout1).isNotEqualTo(workout2);
        workout1.setId(null);
        assertThat(workout1).isNotEqualTo(workout2);
    }
}
