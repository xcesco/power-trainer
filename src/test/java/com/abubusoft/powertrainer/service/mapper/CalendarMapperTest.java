package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalendarMapperTest {

    private CalendarMapper calendarMapper;

    @BeforeEach
    public void setUp() {
        calendarMapper = new CalendarMapperImpl();
    }
}
