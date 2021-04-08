package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.MisurationType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MisurationType}.
 */
public interface MisurationTypeService {
    /**
     * Save a misurationType.
     *
     * @param misurationType the entity to save.
     * @return the persisted entity.
     */
    MisurationType save(MisurationType misurationType);

    /**
     * Partially updates a misurationType.
     *
     * @param misurationType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MisurationType> partialUpdate(MisurationType misurationType);

    /**
     * Get all the misurationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MisurationType> findAll(Pageable pageable);

    /**
     * Get the "id" misurationType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MisurationType> findOne(Long id);

    /**
     * Delete the "id" misurationType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
