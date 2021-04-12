package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Translation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Translation}.
 */
public interface TranslationService {
    /**
     * Save a translation.
     *
     * @param translation the entity to save.
     * @return the persisted entity.
     */
    Translation save(Translation translation);

    /**
     * Partially updates a translation.
     *
     * @param translation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Translation> partialUpdate(Translation translation);

    /**
     * Get all the translations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Translation> findAll(Pageable pageable);

    /**
     * Get the "id" translation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Translation> findOne(Long id);

    /**
     * Delete the "id" translation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
