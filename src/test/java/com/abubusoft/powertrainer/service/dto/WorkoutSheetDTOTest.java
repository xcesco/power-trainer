package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutSheetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutSheetDTO.class);
        WorkoutSheetDTO workoutSheetDTO1 = new WorkoutSheetDTO();
        workoutSheetDTO1.setId(1L);
        WorkoutSheetDTO workoutSheetDTO2 = new WorkoutSheetDTO();
        assertThat(workoutSheetDTO1).isNotEqualTo(workoutSheetDTO2);
        workoutSheetDTO2.setId(workoutSheetDTO1.getId());
        assertThat(workoutSheetDTO1).isEqualTo(workoutSheetDTO2);
        workoutSheetDTO2.setId(2L);
        assertThat(workoutSheetDTO1).isNotEqualTo(workoutSheetDTO2);
        workoutSheetDTO1.setId(null);
        assertThat(workoutSheetDTO1).isNotEqualTo(workoutSheetDTO2);
    }
}
