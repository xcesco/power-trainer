package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.ExerciseDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exercise} and its DTO {@link ExerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = { MuscleMapper.class, ExerciseToolMapper.class })
public interface ExerciseMapper extends EntityMapper<ExerciseDTO, Exercise> {
    @Mapping(target = "muscles", source = "muscles", qualifiedByName = "idSet")
    @Mapping(target = "exerciseTools", source = "exerciseTools", qualifiedByName = "idSet")
    ExerciseDTO toDto(Exercise s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExerciseDTO toDtoId(Exercise exercise);

    @Mapping(target = "removeMuscle", ignore = true)
    @Mapping(target = "removeExerciseTool", ignore = true)
    Exercise toEntity(ExerciseDTO exerciseDTO);
}
