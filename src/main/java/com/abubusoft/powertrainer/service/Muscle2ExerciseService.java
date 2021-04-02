package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Muscle2Exercise}.
 */
public interface Muscle2ExerciseService {
    /**
     * Save a muscle2Exercise.
     *
     * @param muscle2Exercise the entity to save.
     * @return the persisted entity.
     */
    Muscle2Exercise save(Muscle2Exercise muscle2Exercise);

    /**
     * Partially updates a muscle2Exercise.
     *
     * @param muscle2Exercise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Muscle2Exercise> partialUpdate(Muscle2Exercise muscle2Exercise);

    /**
     * Get all the muscle2Exercises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Muscle2Exercise> findAll(Pageable pageable);

    /**
     * Get the "id" muscle2Exercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Muscle2Exercise> findOne(Long id);

    /**
     * Delete the "id" muscle2Exercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
