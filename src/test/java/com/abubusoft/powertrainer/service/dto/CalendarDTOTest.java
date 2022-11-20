package com.abubusoft.powertrainer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abubusoft.powertrainer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarDTO.class);
        CalendarDTO calendarDTO1 = new CalendarDTO();
        calendarDTO1.setId(1L);
        CalendarDTO calendarDTO2 = new CalendarDTO();
        assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
        calendarDTO2.setId(calendarDTO1.getId());
        assertThat(calendarDTO1).isEqualTo(calendarDTO2);
        calendarDTO2.setId(2L);
        assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
        calendarDTO1.setId(null);
        assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
    }
}
