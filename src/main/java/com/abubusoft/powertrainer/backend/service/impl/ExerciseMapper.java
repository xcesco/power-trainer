package com.abubusoft.powertrainer.backend.service.impl;

import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.model.ExerciseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {
  ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

  @Mapping(expression = "java(entity.getDescriptions().get(language))", target = "description")
  ExerciseDto exerciseToDto(Exercise entity, LanguageType language);

}
