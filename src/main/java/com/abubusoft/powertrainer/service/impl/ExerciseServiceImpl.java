package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.repository.ExerciseRepository;
import com.abubusoft.powertrainer.service.ExerciseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exercise}.
 */
@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

    private final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Exercise save(Exercise exercise) {
        log.debug("Request to save Exercise : {}", exercise);
        return exerciseRepository.save(exercise);
    }

    @Override
    public Optional<Exercise> partialUpdate(Exercise exercise) {
        log.debug("Request to partially update Exercise : {}", exercise);

        return exerciseRepository
            .findById(exercise.getId())
            .map(
                existingExercise -> {
                    if (exercise.getUuid() != null) {
                        existingExercise.setUuid(exercise.getUuid());
                    }
                    if (exercise.getImage() != null) {
                        existingExercise.setImage(exercise.getImage());
                    }
                    if (exercise.getImageContentType() != null) {
                        existingExercise.setImageContentType(exercise.getImageContentType());
                    }
                    if (exercise.getName() != null) {
                        existingExercise.setName(exercise.getName());
                    }
                    if (exercise.getDescription() != null) {
                        existingExercise.setDescription(exercise.getDescription());
                    }
                    if (exercise.getValueType() != null) {
                        existingExercise.setValueType(exercise.getValueType());
                    }

                    return existingExercise;
                }
            )
            .map(exerciseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findAll(Pageable pageable) {
        log.debug("Request to get all Exercises");
        return exerciseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exercise> findOne(Long id) {
        log.debug("Request to get Exercise : {}", id);
        return exerciseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
    }
}
