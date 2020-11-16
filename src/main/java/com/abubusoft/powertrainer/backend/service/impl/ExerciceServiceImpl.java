package com.abubusoft.powertrainer.backend.service.impl;

import com.abubusoft.powertrainer.backend.repositories.ExercisesRepository;
import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.service.ExerciseService;
import com.abubusoft.powertrainer.backend.service.model.ExerciseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ExerciceServiceImpl implements ExerciseService {
  private final ExercisesRepository exercisesRepository;

  ExerciceServiceImpl(@Autowired ExercisesRepository exercisesRepository) {
    this.exercisesRepository = exercisesRepository;
  }

  public Page<Exercise> findByName(String name, Pageable pageable) {
    return exercisesRepository.findByName(name, pageable);
  }

  @Override
  public ExerciseDto findByUUID(String customerUUID, LanguageType language) {
    return ExerciseMapper.INSTANCE.exerciseToDto(exercisesRepository.findByUUID(customerUUID).orElse(new Exercise()), language);
  }

  @Override
  public Page<ExerciseDto> findAll(Pageable pageable, LanguageType language) {
    return exercisesRepository.findAll(pageable).map(item -> ExerciseMapper.INSTANCE.exerciseToDto(item, language));
  }
}
