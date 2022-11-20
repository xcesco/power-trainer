package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TranslationMapperTest {

    private TranslationMapper translationMapper;

    @BeforeEach
    public void setUp() {
        translationMapper = new TranslationMapperImpl();
    }
}
