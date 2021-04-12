package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.ExerciseValue;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseValue}.
 */
public interface ExerciseValueService {
    /**
     * Save a exerciseValue.
     *
     * @param exerciseValue the entity to save.
     * @return the persisted entity.
     */
    ExerciseValue save(ExerciseValue exerciseValue);

    /**
     * Partially updates a exerciseValue.
     *
     * @param exerciseValue the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseValue> partialUpdate(ExerciseValue exerciseValue);

    /**
     * Get all the exerciseValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseValue> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseValue> findOne(Long id);

    /**
     * Delete the "id" exerciseValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
