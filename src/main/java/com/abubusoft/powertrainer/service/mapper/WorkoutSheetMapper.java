package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkoutSheet} and its DTO {@link WorkoutSheetDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkoutSheetMapper extends EntityMapper<WorkoutSheetDTO, WorkoutSheet> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkoutSheetDTO toDtoId(WorkoutSheet workoutSheet);
}
