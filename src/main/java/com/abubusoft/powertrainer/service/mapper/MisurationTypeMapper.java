package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MisurationType} and its DTO {@link MisurationTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MisurationTypeMapper extends EntityMapper<MisurationTypeDTO, MisurationType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MisurationTypeDTO toDtoId(MisurationType misurationType);
}
