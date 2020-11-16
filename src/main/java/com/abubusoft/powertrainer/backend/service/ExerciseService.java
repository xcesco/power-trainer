package com.abubusoft.powertrainer.backend.service;

import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.service.model.ExerciseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseService {
  ExerciseDto findByUUID(String customerUUID, LanguageType language);

  Page<ExerciseDto> findAll(Pageable pageable, LanguageType language);
}
