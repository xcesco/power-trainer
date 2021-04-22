package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.service.dto.CalendarDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abubusoft.powertrainer.domain.Calendar}.
 */
public interface CalendarService {
    /**
     * Save a calendar.
     *
     * @param calendarDTO the entity to save.
     * @return the persisted entity.
     */
    CalendarDTO save(CalendarDTO calendarDTO);

    /**
     * Partially updates a calendar.
     *
     * @param calendarDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CalendarDTO> partialUpdate(CalendarDTO calendarDTO);

    /**
     * Get all the calendars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalendarDTO> findAll(Pageable pageable);

    /**
     * Get the "id" calendar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalendarDTO> findOne(Long id);

    /**
     * Delete the "id" calendar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
