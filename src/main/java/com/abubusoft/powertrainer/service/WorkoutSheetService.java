package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.WorkoutSheetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.WorkoutSheet}.
 */
public interface WorkoutSheetService {
    /**
     * Save a workoutSheet.
     *
     * @param workoutSheetDTO the entity to save.
     * @return the persisted entity.
     */
    WorkoutSheetDTO save(WorkoutSheetDTO workoutSheetDTO);

    /**
     * Partially updates a workoutSheet.
     *
     * @param workoutSheetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutSheetDTO> partialUpdate(WorkoutSheetDTO workoutSheetDTO);

    /**
     * Get all the workoutSheets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutSheetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workoutSheet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutSheetDTO> findOne(Long id);

    /**
     * Delete the "id" workoutSheet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
