package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workout} and its DTO {@link WorkoutDTO}.
 */
@Mapper(componentModel = "spring", uses = { CalendarMapper.class })
public interface WorkoutMapper extends EntityMapper<WorkoutDTO, Workout> {
    @Mapping(target = "calendar", source = "calendar", qualifiedByName = "id")
    WorkoutDTO toDto(Workout s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkoutDTO toDtoId(Workout workout);
}
