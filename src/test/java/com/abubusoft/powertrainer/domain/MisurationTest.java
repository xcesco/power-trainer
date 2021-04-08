package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Misuration.class);
        Misuration misuration1 = new Misuration();
        misuration1.setId(1L);
        Misuration misuration2 = new Misuration();
        misuration2.setId(misuration1.getId());
        assertThat(misuration1).isEqualTo(misuration2);
        misuration2.setId(2L);
        assertThat(misuration1).isNotEqualTo(misuration2);
        misuration1.setId(null);
        assertThat(misuration1).isNotEqualTo(misuration2);
    }
}
