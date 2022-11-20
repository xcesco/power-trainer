package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.MuscleDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Muscle} and its DTO {@link MuscleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MuscleMapper extends EntityMapper<MuscleDTO, Muscle> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<MuscleDTO> toDtoIdSet(Set<Muscle> muscle);
}
