package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.MisurationType}.
 */
public interface MisurationTypeService {
    /**
     * Save a misurationType.
     *
     * @param misurationTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MisurationTypeDTO save(MisurationTypeDTO misurationTypeDTO);

    /**
     * Partially updates a misurationType.
     *
     * @param misurationTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MisurationTypeDTO> partialUpdate(MisurationTypeDTO misurationTypeDTO);

    /**
     * Get all the misurationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MisurationTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" misurationType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MisurationTypeDTO> findOne(Long id);

    /**
     * Delete the "id" misurationType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
