package com.abubusoft.powertrainer.backend.service.impl;

import com.abubusoft.powertrainer.backend.model.ExerciseDto;
import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.model.LanguageType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {
  ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

  @Mappings({
          @Mapping(expression = "java(entity.getDescriptions().get(language))", target = "description")
  })
  ExerciseDto exerciseToDto(Exercise entity, LanguageType language);

}
