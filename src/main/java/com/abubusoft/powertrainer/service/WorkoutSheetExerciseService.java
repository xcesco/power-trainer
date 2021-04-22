package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.WorkoutSheetExerciseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.WorkoutSheetExercise}.
 */
public interface WorkoutSheetExerciseService {
    /**
     * Save a workoutSheetExercise.
     *
     * @param workoutSheetExerciseDTO the entity to save.
     * @return the persisted entity.
     */
    WorkoutSheetExerciseDTO save(WorkoutSheetExerciseDTO workoutSheetExerciseDTO);

    /**
     * Partially updates a workoutSheetExercise.
     *
     * @param workoutSheetExerciseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutSheetExerciseDTO> partialUpdate(WorkoutSheetExerciseDTO workoutSheetExerciseDTO);

    /**
     * Get all the workoutSheetExercises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutSheetExerciseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workoutSheetExercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutSheetExerciseDTO> findOne(Long id);

    /**
     * Delete the "id" workoutSheetExercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
