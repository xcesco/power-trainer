package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExerciseValue} and its DTO {@link ExerciseValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { CalendarMapper.class })
public interface ExerciseValueMapper extends EntityMapper<ExerciseValueDTO, ExerciseValue> {
    @Mapping(target = "calendar", source = "calendar", qualifiedByName = "id")
    ExerciseValueDTO toDto(ExerciseValue s);
}
