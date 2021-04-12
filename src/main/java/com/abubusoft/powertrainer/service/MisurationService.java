package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Misuration;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Misuration}.
 */
public interface MisurationService {
    /**
     * Save a misuration.
     *
     * @param misuration the entity to save.
     * @return the persisted entity.
     */
    Misuration save(Misuration misuration);

    /**
     * Partially updates a misuration.
     *
     * @param misuration the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Misuration> partialUpdate(Misuration misuration);

    /**
     * Get all the misurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Misuration> findAll(Pageable pageable);

    /**
     * Get the "id" misuration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Misuration> findOne(Long id);

    /**
     * Delete the "id" misuration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
