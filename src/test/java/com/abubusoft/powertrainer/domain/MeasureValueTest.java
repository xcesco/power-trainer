package com.abubusoft.powertrainer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeasureValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasureValue.class);
        MeasureValue measureValue1 = new MeasureValue();
        measureValue1.setId(1L);
        MeasureValue measureValue2 = new MeasureValue();
        measureValue2.setId(measureValue1.getId());
        assertThat(measureValue1).isEqualTo(measureValue2);
        measureValue2.setId(2L);
        assertThat(measureValue1).isNotEqualTo(measureValue2);
        measureValue1.setId(null);
        assertThat(measureValue1).isNotEqualTo(measureValue2);
    }
}
