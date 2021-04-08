package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisurationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MisurationType.class);
        MisurationType misurationType1 = new MisurationType();
        misurationType1.setId(1L);
        MisurationType misurationType2 = new MisurationType();
        misurationType2.setId(misurationType1.getId());
        assertThat(misurationType1).isEqualTo(misurationType2);
        misurationType2.setId(2L);
        assertThat(misurationType1).isNotEqualTo(misurationType2);
        misurationType1.setId(null);
        assertThat(misurationType1).isNotEqualTo(misurationType2);
    }
}
