package com.abubusoft.powertrainer.backend.service.impl;

import com.abubusoft.powertrainer.backend.model.ExerciseDto;
import com.abubusoft.powertrainer.backend.repositories.ExercisesRepository;
import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Optional;

@Service
public class ExerciceServiceImpl implements ExerciseService {
  private final ExercisesRepository exercisesRepository;

  ExerciceServiceImpl(@Autowired ExercisesRepository exercisesRepository) {
    this.exercisesRepository = exercisesRepository;
  }

  @Override
  public ExerciseDto findByUUID(String customerUUID, LanguageType language) {
    return ExerciseMapper.INSTANCE.exerciseToDto(exercisesRepository.findByUUID(customerUUID).orElse(new Exercise()), language);
  }

  @Override
  public Page<ExerciseDto> findAll(LanguageType language, Pageable pageable) {
    return exercisesRepository
            .findAll(pageable)
            .map(item -> ExerciseMapper.INSTANCE
                    .exerciseToDto(item, language));
  }

  @Override
  public Resource findImageByUUID(String exerciseUUID) {
    Optional<Exercise> exercise = exercisesRepository.findByUUID(exerciseUUID);

    if (exercise.isPresent()) {
      Resource resource;
      try {
        resource = new FileUrlResource(exercise.get().getImage());
        return resource;
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }

    }
    return null;
  }

  @Override
  public Page<ExerciseDto> findByName(String name, LanguageType language, Pageable pageable) {
    return exercisesRepository
            .findByName(name, pageable)
            .map(item -> ExerciseMapper.INSTANCE
                    .exerciseToDto(item, language));
  }

  @Override
  public Page<ExerciseDto> findByMuscle(String muscle, LanguageType language, Pageable pageable) {
    return exercisesRepository
            .findByMuscle(muscle, pageable)
            .map(item -> ExerciseMapper.INSTANCE
                    .exerciseToDto(item, language));
  }
}
