package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.repository.CalendarRepository;
import com.abubusoft.powertrainer.service.CalendarService;
import com.abubusoft.powertrainer.service.dto.CalendarDTO;
import com.abubusoft.powertrainer.service.mapper.CalendarMapper;
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

    private final CalendarMapper calendarMapper;

    public CalendarServiceImpl(CalendarRepository calendarRepository, CalendarMapper calendarMapper) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
    }

    @Override
    public CalendarDTO save(CalendarDTO calendarDTO) {
        log.debug("Request to save Calendar : {}", calendarDTO);
        Calendar calendar = calendarMapper.toEntity(calendarDTO);
        calendar = calendarRepository.save(calendar);
        return calendarMapper.toDto(calendar);
    }

    @Override
    public Optional<CalendarDTO> partialUpdate(CalendarDTO calendarDTO) {
        log.debug("Request to partially update Calendar : {}", calendarDTO);

        return calendarRepository
            .findById(calendarDTO.getId())
            .map(
                existingCalendar -> {
                    calendarMapper.partialUpdate(existingCalendar, calendarDTO);
                    return existingCalendar;
                }
            )
            .map(calendarRepository::save)
            .map(calendarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalendarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Calendars");
        return calendarRepository.findAll(pageable).map(calendarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarDTO> findOne(Long id) {
        log.debug("Request to get Calendar : {}", id);
        return calendarRepository.findById(id).map(calendarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calendar : {}", id);
        calendarRepository.deleteById(id);
    }
}
