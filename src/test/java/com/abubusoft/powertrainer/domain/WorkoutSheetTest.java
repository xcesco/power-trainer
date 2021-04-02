package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutSheet.class);
        WorkoutSheet workoutSheet1 = new WorkoutSheet();
        workoutSheet1.setId(1L);
        WorkoutSheet workoutSheet2 = new WorkoutSheet();
        workoutSheet2.setId(workoutSheet1.getId());
        assertThat(workoutSheet1).isEqualTo(workoutSheet2);
        workoutSheet2.setId(2L);
        assertThat(workoutSheet1).isNotEqualTo(workoutSheet2);
        workoutSheet1.setId(null);
        assertThat(workoutSheet1).isNotEqualTo(workoutSheet2);
    }
}
