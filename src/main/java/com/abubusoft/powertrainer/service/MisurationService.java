package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.MisurationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.Misuration}.
 */
public interface MisurationService {
    /**
     * Save a misuration.
     *
     * @param misurationDTO the entity to save.
     * @return the persisted entity.
     */
    MisurationDTO save(MisurationDTO misurationDTO);

    /**
     * Partially updates a misuration.
     *
     * @param misurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MisurationDTO> partialUpdate(MisurationDTO misurationDTO);

    /**
     * Get all the misurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MisurationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" misuration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MisurationDTO> findOne(Long id);

    /**
     * Delete the "id" misuration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
