package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.ExerciseResourceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.ExerciseResource}.
 */
public interface ExerciseResourceService {
    /**
     * Save a exerciseResource.
     *
     * @param exerciseResourceDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseResourceDTO save(ExerciseResourceDTO exerciseResourceDTO);

    /**
     * Partially updates a exerciseResource.
     *
     * @param exerciseResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseResourceDTO> partialUpdate(ExerciseResourceDTO exerciseResourceDTO);

    /**
     * Get all the exerciseResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseResourceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseResourceDTO> findOne(Long id);

    /**
     * Delete the "id" exerciseResource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
