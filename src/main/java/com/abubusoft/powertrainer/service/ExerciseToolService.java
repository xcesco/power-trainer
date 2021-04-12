package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.ExerciseTool;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseTool}.
 */
public interface ExerciseToolService {
    /**
     * Save a exerciseTool.
     *
     * @param exerciseTool the entity to save.
     * @return the persisted entity.
     */
    ExerciseTool save(ExerciseTool exerciseTool);

    /**
     * Partially updates a exerciseTool.
     *
     * @param exerciseTool the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseTool> partialUpdate(ExerciseTool exerciseTool);

    /**
     * Get all the exerciseTools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseTool> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseTool.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseTool> findOne(Long id);

    /**
     * Delete the "id" exerciseTool.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
