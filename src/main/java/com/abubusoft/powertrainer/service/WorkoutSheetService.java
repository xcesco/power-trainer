package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.WorkoutSheet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WorkoutSheet}.
 */
public interface WorkoutSheetService {
    /**
     * Save a workoutSheet.
     *
     * @param workoutSheet the entity to save.
     * @return the persisted entity.
     */
    WorkoutSheet save(WorkoutSheet workoutSheet);

    /**
     * Partially updates a workoutSheet.
     *
     * @param workoutSheet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutSheet> partialUpdate(WorkoutSheet workoutSheet);

    /**
     * Get all the workoutSheets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutSheet> findAll(Pageable pageable);

    /**
     * Get the "id" workoutSheet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutSheet> findOne(Long id);

    /**
     * Delete the "id" workoutSheet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
