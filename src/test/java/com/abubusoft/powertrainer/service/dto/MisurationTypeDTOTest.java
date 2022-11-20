package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisurationTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MisurationTypeDTO.class);
        MisurationTypeDTO misurationTypeDTO1 = new MisurationTypeDTO();
        misurationTypeDTO1.setId(1L);
        MisurationTypeDTO misurationTypeDTO2 = new MisurationTypeDTO();
        assertThat(misurationTypeDTO1).isNotEqualTo(misurationTypeDTO2);
        misurationTypeDTO2.setId(misurationTypeDTO1.getId());
        assertThat(misurationTypeDTO1).isEqualTo(misurationTypeDTO2);
        misurationTypeDTO2.setId(2L);
        assertThat(misurationTypeDTO1).isNotEqualTo(misurationTypeDTO2);
        misurationTypeDTO1.setId(null);
        assertThat(misurationTypeDTO1).isNotEqualTo(misurationTypeDTO2);
    }
}
