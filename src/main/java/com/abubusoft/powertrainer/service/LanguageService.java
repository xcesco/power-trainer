package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Language;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Language}.
 */
public interface LanguageService {
    /**
     * Save a language.
     *
     * @param language the entity to save.
     * @return the persisted entity.
     */
    Language save(Language language);

    /**
     * Partially updates a language.
     *
     * @param language the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Language> partialUpdate(Language language);

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Language> findAll(Pageable pageable);

    /**
     * Get the "id" language.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Language> findOne(Long id);

    /**
     * Delete the "id" language.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
