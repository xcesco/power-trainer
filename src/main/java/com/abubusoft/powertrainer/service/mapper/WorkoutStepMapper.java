package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.WorkoutStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkoutStep} and its DTO {@link WorkoutStepDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkoutMapper.class })
public interface WorkoutStepMapper extends EntityMapper<WorkoutStepDTO, WorkoutStep> {
    @Mapping(target = "workout", source = "workout", qualifiedByName = "id")
    WorkoutStepDTO toDto(WorkoutStep s);
}
