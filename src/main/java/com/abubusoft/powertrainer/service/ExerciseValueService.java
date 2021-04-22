package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.ExerciseValue}.
 */
public interface ExerciseValueService {
    /**
     * Save a exerciseValue.
     *
     * @param exerciseValueDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseValueDTO save(ExerciseValueDTO exerciseValueDTO);

    /**
     * Partially updates a exerciseValue.
     *
     * @param exerciseValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseValueDTO> partialUpdate(ExerciseValueDTO exerciseValueDTO);

    /**
     * Get all the exerciseValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseValueDTO> findOne(Long id);

    /**
     * Delete the "id" exerciseValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
