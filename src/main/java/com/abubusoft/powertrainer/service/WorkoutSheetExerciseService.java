package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WorkoutSheetExercise}.
 */
public interface WorkoutSheetExerciseService {
    /**
     * Save a workoutSheetExercise.
     *
     * @param workoutSheetExercise the entity to save.
     * @return the persisted entity.
     */
    WorkoutSheetExercise save(WorkoutSheetExercise workoutSheetExercise);

    /**
     * Partially updates a workoutSheetExercise.
     *
     * @param workoutSheetExercise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutSheetExercise> partialUpdate(WorkoutSheetExercise workoutSheetExercise);

    /**
     * Get all the workoutSheetExercises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutSheetExercise> findAll(Pageable pageable);

    /**
     * Get the "id" workoutSheetExercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutSheetExercise> findOne(Long id);

    /**
     * Delete the "id" workoutSheetExercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
