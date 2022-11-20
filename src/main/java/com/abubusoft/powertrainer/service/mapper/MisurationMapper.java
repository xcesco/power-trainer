package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.MisurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Misuration} and its DTO {@link MisurationDTO}.
 */
@Mapper(componentModel = "spring", uses = { CalendarMapper.class, MisurationTypeMapper.class })
public interface MisurationMapper extends EntityMapper<MisurationDTO, Misuration> {
    @Mapping(target = "calendar", source = "calendar", qualifiedByName = "id")
    @Mapping(target = "misurationType", source = "misurationType", qualifiedByName = "id")
    MisurationDTO toDto(Misuration s);
}
