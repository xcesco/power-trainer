package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.WorkoutStepDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.WorkoutStep}.
 */
public interface WorkoutStepService {
    /**
     * Save a workoutStep.
     *
     * @param workoutStepDTO the entity to save.
     * @return the persisted entity.
     */
    WorkoutStepDTO save(WorkoutStepDTO workoutStepDTO);

    /**
     * Partially updates a workoutStep.
     *
     * @param workoutStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutStepDTO> partialUpdate(WorkoutStepDTO workoutStepDTO);

    /**
     * Get all the workoutSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutStepDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workoutStep.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutStepDTO> findOne(Long id);

    /**
     * Delete the "id" workoutStep.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
