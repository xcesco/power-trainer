package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Translation.class);
        Translation translation1 = new Translation();
        translation1.setId(1L);
        Translation translation2 = new Translation();
        translation2.setId(translation1.getId());
        assertThat(translation1).isEqualTo(translation2);
        translation2.setId(2L);
        assertThat(translation1).isNotEqualTo(translation2);
        translation1.setId(null);
        assertThat(translation1).isNotEqualTo(translation2);
    }
}
