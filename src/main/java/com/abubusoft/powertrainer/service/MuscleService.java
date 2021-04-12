package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Muscle;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Muscle}.
 */
public interface MuscleService {
    /**
     * Save a muscle.
     *
     * @param muscle the entity to save.
     * @return the persisted entity.
     */
    Muscle save(Muscle muscle);

    /**
     * Partially updates a muscle.
     *
     * @param muscle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Muscle> partialUpdate(Muscle muscle);

    /**
     * Get all the muscles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Muscle> findAll(Pageable pageable);

    /**
     * Get the "id" muscle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Muscle> findOne(Long id);

    /**
     * Delete the "id" muscle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
