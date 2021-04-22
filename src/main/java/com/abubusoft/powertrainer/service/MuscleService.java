package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.MuscleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.Muscle}.
 */
public interface MuscleService {
    /**
     * Save a muscle.
     *
     * @param muscleDTO the entity to save.
     * @return the persisted entity.
     */
    MuscleDTO save(MuscleDTO muscleDTO);

    /**
     * Partially updates a muscle.
     *
     * @param muscleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MuscleDTO> partialUpdate(MuscleDTO muscleDTO);

    /**
     * Get all the muscles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MuscleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" muscle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MuscleDTO> findOne(Long id);

    /**
     * Delete the "id" muscle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
