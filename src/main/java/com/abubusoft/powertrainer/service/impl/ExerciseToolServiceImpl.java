package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.repository.ExerciseToolRepository;
import com.abubusoft.powertrainer.service.ExerciseToolService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseTool}.
 */
@Service
@Transactional
public class ExerciseToolServiceImpl implements ExerciseToolService {

    private final Logger log = LoggerFactory.getLogger(ExerciseToolServiceImpl.class);

    private final ExerciseToolRepository exerciseToolRepository;

    public ExerciseToolServiceImpl(ExerciseToolRepository exerciseToolRepository) {
        this.exerciseToolRepository = exerciseToolRepository;
    }

    @Override
    public ExerciseTool save(ExerciseTool exerciseTool) {
        log.debug("Request to save ExerciseTool : {}", exerciseTool);
        return exerciseToolRepository.save(exerciseTool);
    }

    @Override
    public Optional<ExerciseTool> partialUpdate(ExerciseTool exerciseTool) {
        log.debug("Request to partially update ExerciseTool : {}", exerciseTool);

        return exerciseToolRepository
            .findById(exerciseTool.getId())
            .map(
                existingExerciseTool -> {
                    if (exerciseTool.getUuid() != null) {
                        existingExerciseTool.setUuid(exerciseTool.getUuid());
                    }
                    if (exerciseTool.getImage() != null) {
                        existingExerciseTool.setImage(exerciseTool.getImage());
                    }
                    if (exerciseTool.getImageContentType() != null) {
                        existingExerciseTool.setImageContentType(exerciseTool.getImageContentType());
                    }
                    if (exerciseTool.getName() != null) {
                        existingExerciseTool.setName(exerciseTool.getName());
                    }
                    if (exerciseTool.getDescription() != null) {
                        existingExerciseTool.setDescription(exerciseTool.getDescription());
                    }

                    return existingExerciseTool;
                }
            )
            .map(exerciseToolRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseTool> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseTools");
        return exerciseToolRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseTool> findOne(Long id) {
        log.debug("Request to get ExerciseTool : {}", id);
        return exerciseToolRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseTool : {}", id);
        exerciseToolRepository.deleteById(id);
    }
}
