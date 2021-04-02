package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.WorkoutStep;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WorkoutStep}.
 */
public interface WorkoutStepService {
    /**
     * Save a workoutStep.
     *
     * @param workoutStep the entity to save.
     * @return the persisted entity.
     */
    WorkoutStep save(WorkoutStep workoutStep);

    /**
     * Partially updates a workoutStep.
     *
     * @param workoutStep the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutStep> partialUpdate(WorkoutStep workoutStep);

    /**
     * Get all the workoutSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutStep> findAll(Pageable pageable);

    /**
     * Get the "id" workoutStep.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutStep> findOne(Long id);

    /**
     * Delete the "id" workoutStep.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
