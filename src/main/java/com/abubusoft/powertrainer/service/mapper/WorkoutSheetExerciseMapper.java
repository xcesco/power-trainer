package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetExerciseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkoutSheetExercise} and its DTO {@link WorkoutSheetExerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkoutSheetMapper.class })
public interface WorkoutSheetExerciseMapper extends EntityMapper<WorkoutSheetExerciseDTO, WorkoutSheetExercise> {
    @Mapping(target = "workoutSheet", source = "workoutSheet", qualifiedByName = "id")
    WorkoutSheetExerciseDTO toDto(WorkoutSheetExercise s);
}
