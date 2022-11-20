package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.ExerciseResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExerciseResource} and its DTO {@link ExerciseResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ExerciseMapper.class })
public interface ExerciseResourceMapper extends EntityMapper<ExerciseResourceDTO, ExerciseResource> {
    @Mapping(target = "exercise", source = "exercise", qualifiedByName = "id")
    ExerciseResourceDTO toDto(ExerciseResource s);
}
