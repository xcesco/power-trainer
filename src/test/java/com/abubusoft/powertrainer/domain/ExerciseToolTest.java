package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseToolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseTool.class);
        ExerciseTool exerciseTool1 = new ExerciseTool();
        exerciseTool1.setId(1L);
        ExerciseTool exerciseTool2 = new ExerciseTool();
        exerciseTool2.setId(exerciseTool1.getId());
        assertThat(exerciseTool1).isEqualTo(exerciseTool2);
        exerciseTool2.setId(2L);
        assertThat(exerciseTool1).isNotEqualTo(exerciseTool2);
        exerciseTool1.setId(null);
        assertThat(exerciseTool1).isNotEqualTo(exerciseTool2);
    }
}
