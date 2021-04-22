package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.ExerciseToolDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.ExerciseTool}.
 */
public interface ExerciseToolService {
    /**
     * Save a exerciseTool.
     *
     * @param exerciseToolDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseToolDTO save(ExerciseToolDTO exerciseToolDTO);

    /**
     * Partially updates a exerciseTool.
     *
     * @param exerciseToolDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseToolDTO> partialUpdate(ExerciseToolDTO exerciseToolDTO);

    /**
     * Get all the exerciseTools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseToolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseTool.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseToolDTO> findOne(Long id);

    /**
     * Delete the "id" exerciseTool.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
