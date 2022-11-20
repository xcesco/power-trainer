package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.TranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Translation} and its DTO {@link TranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { LanguageMapper.class })
public interface TranslationMapper extends EntityMapper<TranslationDTO, Translation> {
    @Mapping(target = "language", source = "language", qualifiedByName = "id")
    TranslationDTO toDto(Translation s);
}
