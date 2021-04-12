package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import com.abubusoft.powertrainer.repository.WorkoutSheetExerciseRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetExerciseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutSheetExercise}.
 */
@Service
@Transactional
public class WorkoutSheetExerciseServiceImpl implements WorkoutSheetExerciseService {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetExerciseServiceImpl.class);

    private final WorkoutSheetExerciseRepository workoutSheetExerciseRepository;

    public WorkoutSheetExerciseServiceImpl(WorkoutSheetExerciseRepository workoutSheetExerciseRepository) {
        this.workoutSheetExerciseRepository = workoutSheetExerciseRepository;
    }

    @Override
    public WorkoutSheetExercise save(WorkoutSheetExercise workoutSheetExercise) {
        log.debug("Request to save WorkoutSheetExercise : {}", workoutSheetExercise);
        return workoutSheetExerciseRepository.save(workoutSheetExercise);
    }

    @Override
    public Optional<WorkoutSheetExercise> partialUpdate(WorkoutSheetExercise workoutSheetExercise) {
        log.debug("Request to partially update WorkoutSheetExercise : {}", workoutSheetExercise);

        return workoutSheetExerciseRepository
            .findById(workoutSheetExercise.getId())
            .map(
                existingWorkoutSheetExercise -> {
                    if (workoutSheetExercise.getUuid() != null) {
                        existingWorkoutSheetExercise.setUuid(workoutSheetExercise.getUuid());
                    }
                    if (workoutSheetExercise.getOrder() != null) {
                        existingWorkoutSheetExercise.setOrder(workoutSheetExercise.getOrder());
                    }
                    if (workoutSheetExercise.getRepetitions() != null) {
                        existingWorkoutSheetExercise.setRepetitions(workoutSheetExercise.getRepetitions());
                    }
                    if (workoutSheetExercise.getExerciseUuid() != null) {
                        existingWorkoutSheetExercise.setExerciseUuid(workoutSheetExercise.getExerciseUuid());
                    }
                    if (workoutSheetExercise.getExerciseName() != null) {
                        existingWorkoutSheetExercise.setExerciseName(workoutSheetExercise.getExerciseName());
                    }
                    if (workoutSheetExercise.getExerciseValue() != null) {
                        existingWorkoutSheetExercise.setExerciseValue(workoutSheetExercise.getExerciseValue());
                    }
                    if (workoutSheetExercise.getExerciseValueType() != null) {
                        existingWorkoutSheetExercise.setExerciseValueType(workoutSheetExercise.getExerciseValueType());
                    }

                    return existingWorkoutSheetExercise;
                }
            )
            .map(workoutSheetExerciseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutSheetExercise> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSheetExercises");
        return workoutSheetExerciseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutSheetExercise> findOne(Long id) {
        log.debug("Request to get WorkoutSheetExercise : {}", id);
        return workoutSheetExerciseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutSheetExercise : {}", id);
        workoutSheetExerciseRepository.deleteById(id);
    }
}
