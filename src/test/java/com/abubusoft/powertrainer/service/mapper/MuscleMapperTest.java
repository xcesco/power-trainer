package com.abubusoft.powertrainer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MuscleMapperTest {

    private MuscleMapper muscleMapper;

    @BeforeEach
    public void setUp() {
        muscleMapper = new MuscleMapperImpl();
    }
}
