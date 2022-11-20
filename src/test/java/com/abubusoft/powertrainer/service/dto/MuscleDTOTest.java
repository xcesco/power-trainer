package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MuscleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MuscleDTO.class);
        MuscleDTO muscleDTO1 = new MuscleDTO();
        muscleDTO1.setId(1L);
        MuscleDTO muscleDTO2 = new MuscleDTO();
        assertThat(muscleDTO1).isNotEqualTo(muscleDTO2);
        muscleDTO2.setId(muscleDTO1.getId());
        assertThat(muscleDTO1).isEqualTo(muscleDTO2);
        muscleDTO2.setId(2L);
        assertThat(muscleDTO1).isNotEqualTo(muscleDTO2);
        muscleDTO1.setId(null);
        assertThat(muscleDTO1).isNotEqualTo(muscleDTO2);
    }
}
