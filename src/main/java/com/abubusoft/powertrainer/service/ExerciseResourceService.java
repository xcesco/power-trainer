package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.ExerciseResource;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseResource}.
 */
public interface ExerciseResourceService {
    /**
     * Save a exerciseResource.
     *
     * @param exerciseResource the entity to save.
     * @return the persisted entity.
     */
    ExerciseResource save(ExerciseResource exerciseResource);

    /**
     * Partially updates a exerciseResource.
     *
     * @param exerciseResource the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseResource> partialUpdate(ExerciseResource exerciseResource);

    /**
     * Get all the exerciseResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseResource> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseResource> findOne(Long id);

    /**
     * Delete the "id" exerciseResource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
