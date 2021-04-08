package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.ExerciseValueService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseValue}.
 */
@Service
@Transactional
public class ExerciseValueServiceImpl implements ExerciseValueService {

    private final Logger log = LoggerFactory.getLogger(ExerciseValueServiceImpl.class);

    private final ExerciseValueRepository exerciseValueRepository;

    public ExerciseValueServiceImpl(ExerciseValueRepository exerciseValueRepository) {
        this.exerciseValueRepository = exerciseValueRepository;
    }

    @Override
    public ExerciseValue save(ExerciseValue exerciseValue) {
        log.debug("Request to save ExerciseValue : {}", exerciseValue);
        return exerciseValueRepository.save(exerciseValue);
    }

    @Override
    public Optional<ExerciseValue> partialUpdate(ExerciseValue exerciseValue) {
        log.debug("Request to partially update ExerciseValue : {}", exerciseValue);

        return exerciseValueRepository
            .findById(exerciseValue.getId())
            .map(
                existingExerciseValue -> {
                    if (exerciseValue.getUuid() != null) {
                        existingExerciseValue.setUuid(exerciseValue.getUuid());
                    }
                    if (exerciseValue.getDate() != null) {
                        existingExerciseValue.setDate(exerciseValue.getDate());
                    }
                    if (exerciseValue.getExerciseUuid() != null) {
                        existingExerciseValue.setExerciseUuid(exerciseValue.getExerciseUuid());
                    }
                    if (exerciseValue.getExerciseName() != null) {
                        existingExerciseValue.setExerciseName(exerciseValue.getExerciseName());
                    }
                    if (exerciseValue.getExerciseValue() != null) {
                        existingExerciseValue.setExerciseValue(exerciseValue.getExerciseValue());
                    }
                    if (exerciseValue.getExerciseValueType() != null) {
                        existingExerciseValue.setExerciseValueType(exerciseValue.getExerciseValueType());
                    }

                    return existingExerciseValue;
                }
            )
            .map(exerciseValueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseValue> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseValues");
        return exerciseValueRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseValue> findOne(Long id) {
        log.debug("Request to get ExerciseValue : {}", id);
        return exerciseValueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseValue : {}", id);
        exerciseValueRepository.deleteById(id);
    }
}
