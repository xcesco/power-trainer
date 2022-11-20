package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.ExerciseToolDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExerciseTool} and its DTO {@link ExerciseToolDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExerciseToolMapper extends EntityMapper<ExerciseToolDTO, ExerciseTool> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ExerciseToolDTO> toDtoIdSet(Set<ExerciseTool> exerciseTool);
}
