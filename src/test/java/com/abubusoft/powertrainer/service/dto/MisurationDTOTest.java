package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MisurationDTO.class);
        MisurationDTO misurationDTO1 = new MisurationDTO();
        misurationDTO1.setId(1L);
        MisurationDTO misurationDTO2 = new MisurationDTO();
        assertThat(misurationDTO1).isNotEqualTo(misurationDTO2);
        misurationDTO2.setId(misurationDTO1.getId());
        assertThat(misurationDTO1).isEqualTo(misurationDTO2);
        misurationDTO2.setId(2L);
        assertThat(misurationDTO1).isNotEqualTo(misurationDTO2);
        misurationDTO1.setId(null);
        assertThat(misurationDTO1).isNotEqualTo(misurationDTO2);
    }
}
