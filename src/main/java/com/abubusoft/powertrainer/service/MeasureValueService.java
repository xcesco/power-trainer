package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.MeasureValue;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MeasureValue}.
 */
public interface MeasureValueService {
    /**
     * Save a measureValue.
     *
     * @param measureValue the entity to save.
     * @return the persisted entity.
     */
    MeasureValue save(MeasureValue measureValue);

    /**
     * Partially updates a measureValue.
     *
     * @param measureValue the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeasureValue> partialUpdate(MeasureValue measureValue);

    /**
     * Get all the measureValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MeasureValue> findAll(Pageable pageable);

    /**
     * Get the "id" measureValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeasureValue> findOne(Long id);

    /**
     * Delete the "id" measureValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
