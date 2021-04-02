package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.MeasureValue;
import com.abubusoft.powertrainer.repository.MeasureValueRepository;
import com.abubusoft.powertrainer.service.MeasureValueService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MeasureValue}.
 */
@Service
@Transactional
public class MeasureValueServiceImpl implements MeasureValueService {

    private final Logger log = LoggerFactory.getLogger(MeasureValueServiceImpl.class);

    private final MeasureValueRepository measureValueRepository;

    public MeasureValueServiceImpl(MeasureValueRepository measureValueRepository) {
        this.measureValueRepository = measureValueRepository;
    }

    @Override
    public MeasureValue save(MeasureValue measureValue) {
        log.debug("Request to save MeasureValue : {}", measureValue);
        return measureValueRepository.save(measureValue);
    }

    @Override
    public Optional<MeasureValue> partialUpdate(MeasureValue measureValue) {
        log.debug("Request to partially update MeasureValue : {}", measureValue);

        return measureValueRepository
            .findById(measureValue.getId())
            .map(
                existingMeasureValue -> {
                    if (measureValue.getUuid() != null) {
                        existingMeasureValue.setUuid(measureValue.getUuid());
                    }
                    if (measureValue.getDate() != null) {
                        existingMeasureValue.setDate(measureValue.getDate());
                    }
                    if (measureValue.getValue() != null) {
                        existingMeasureValue.setValue(measureValue.getValue());
                    }
                    if (measureValue.getNote() != null) {
                        existingMeasureValue.setNote(measureValue.getNote());
                    }

                    return existingMeasureValue;
                }
            )
            .map(measureValueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasureValue> findAll(Pageable pageable) {
        log.debug("Request to get all MeasureValues");
        return measureValueRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeasureValue> findOne(Long id) {
        log.debug("Request to get MeasureValue : {}", id);
        return measureValueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MeasureValue : {}", id);
        measureValueRepository.deleteById(id);
    }
}
