package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.repository.CalendarRepository;
import com.abubusoft.powertrainer.service.CalendarService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Calendar}.
 */
@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {

    private final Logger log = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private final CalendarRepository calendarRepository;

    public CalendarServiceImpl(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @Override
    public Calendar save(Calendar calendar) {
        log.debug("Request to save Calendar : {}", calendar);
        return calendarRepository.save(calendar);
    }

    @Override
    public Optional<Calendar> partialUpdate(Calendar calendar) {
        log.debug("Request to partially update Calendar : {}", calendar);

        return calendarRepository
            .findById(calendar.getId())
            .map(
                existingCalendar -> {
                    if (calendar.getUuid() != null) {
                        existingCalendar.setUuid(calendar.getUuid());
                    }
                    if (calendar.getName() != null) {
                        existingCalendar.setName(calendar.getName());
                    }

                    return existingCalendar;
                }
            )
            .map(calendarRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Calendar> findAll(Pageable pageable) {
        log.debug("Request to get all Calendars");
        return calendarRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calendar> findOne(Long id) {
        log.debug("Request to get Calendar : {}", id);
        return calendarRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calendar : {}", id);
        calendarRepository.deleteById(id);
    }
}
