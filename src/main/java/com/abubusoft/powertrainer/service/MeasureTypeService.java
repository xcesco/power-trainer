package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.MeasureType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MeasureType}.
 */
public interface MeasureTypeService {
    /**
     * Save a measureType.
     *
     * @param measureType the entity to save.
     * @return the persisted entity.
     */
    MeasureType save(MeasureType measureType);

    /**
     * Partially updates a measureType.
     *
     * @param measureType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeasureType> partialUpdate(MeasureType measureType);

    /**
     * Get all the measureTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MeasureType> findAll(Pageable pageable);

    /**
     * Get the "id" measureType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeasureType> findOne(Long id);

    /**
     * Delete the "id" measureType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
