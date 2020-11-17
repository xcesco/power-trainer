package com.abubusoft.powertrainer.backend.service;

import com.abubusoft.powertrainer.backend.model.ExerciseDto;
import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseService {
  ExerciseDto findByUUID(String customerUUID, LanguageType language);

  Resource findImageByUUID(String exerciseUUID);

  Page<ExerciseDto> findAll(LanguageType language, Pageable pageable);

  Page<ExerciseDto> findByName(String name, LanguageType language, Pageable pageable);

  Page<ExerciseDto> findByMuscle(String muscle, LanguageType language, Pageable pageable);
}
