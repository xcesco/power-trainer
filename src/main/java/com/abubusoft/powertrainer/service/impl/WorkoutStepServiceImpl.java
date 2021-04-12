package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.WorkoutStepService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutStep}.
 */
@Service
@Transactional
public class WorkoutStepServiceImpl implements WorkoutStepService {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepServiceImpl.class);

    private final WorkoutStepRepository workoutStepRepository;

    public WorkoutStepServiceImpl(WorkoutStepRepository workoutStepRepository) {
        this.workoutStepRepository = workoutStepRepository;
    }

    @Override
    public WorkoutStep save(WorkoutStep workoutStep) {
        log.debug("Request to save WorkoutStep : {}", workoutStep);
        return workoutStepRepository.save(workoutStep);
    }

    @Override
    public Optional<WorkoutStep> partialUpdate(WorkoutStep workoutStep) {
        log.debug("Request to partially update WorkoutStep : {}", workoutStep);

        return workoutStepRepository
            .findById(workoutStep.getId())
            .map(
                existingWorkoutStep -> {
                    if (workoutStep.getUuid() != null) {
                        existingWorkoutStep.setUuid(workoutStep.getUuid());
                    }
                    if (workoutStep.getOrder() != null) {
                        existingWorkoutStep.setOrder(workoutStep.getOrder());
                    }
                    if (workoutStep.getExecutionTime() != null) {
                        existingWorkoutStep.setExecutionTime(workoutStep.getExecutionTime());
                    }
                    if (workoutStep.getType() != null) {
                        existingWorkoutStep.setType(workoutStep.getType());
                    }
                    if (workoutStep.getStatus() != null) {
                        existingWorkoutStep.setStatus(workoutStep.getStatus());
                    }
                    if (workoutStep.getExerciseUuid() != null) {
                        existingWorkoutStep.setExerciseUuid(workoutStep.getExerciseUuid());
                    }
                    if (workoutStep.getExerciseName() != null) {
                        existingWorkoutStep.setExerciseName(workoutStep.getExerciseName());
                    }
                    if (workoutStep.getExerciseValue() != null) {
                        existingWorkoutStep.setExerciseValue(workoutStep.getExerciseValue());
                    }
                    if (workoutStep.getExerciseValueType() != null) {
                        existingWorkoutStep.setExerciseValueType(workoutStep.getExerciseValueType());
                    }

                    return existingWorkoutStep;
                }
            )
            .map(workoutStepRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutStep> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSteps");
        return workoutStepRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutStep> findOne(Long id) {
        log.debug("Request to get WorkoutStep : {}", id);
        return workoutStepRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutStep : {}", id);
        workoutStepRepository.deleteById(id);
    }
}
