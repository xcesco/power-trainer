package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Calendar;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Calendar}.
 */
public interface CalendarService {
    /**
     * Save a calendar.
     *
     * @param calendar the entity to save.
     * @return the persisted entity.
     */
    Calendar save(Calendar calendar);

    /**
     * Partially updates a calendar.
     *
     * @param calendar the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Calendar> partialUpdate(Calendar calendar);

    /**
     * Get all the calendars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Calendar> findAll(Pageable pageable);

    /**
     * Get the "id" calendar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Calendar> findOne(Long id);

    /**
     * Delete the "id" calendar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
