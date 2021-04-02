package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.MeasureType;
import com.abubusoft.powertrainer.repository.MeasureTypeRepository;
import com.abubusoft.powertrainer.service.MeasureTypeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MeasureType}.
 */
@Service
@Transactional
public class MeasureTypeServiceImpl implements MeasureTypeService {

    private final Logger log = LoggerFactory.getLogger(MeasureTypeServiceImpl.class);

    private final MeasureTypeRepository measureTypeRepository;

    public MeasureTypeServiceImpl(MeasureTypeRepository measureTypeRepository) {
        this.measureTypeRepository = measureTypeRepository;
    }

    @Override
    public MeasureType save(MeasureType measureType) {
        log.debug("Request to save MeasureType : {}", measureType);
        return measureTypeRepository.save(measureType);
    }

    @Override
    public Optional<MeasureType> partialUpdate(MeasureType measureType) {
        log.debug("Request to partially update MeasureType : {}", measureType);

        return measureTypeRepository
            .findById(measureType.getId())
            .map(
                existingMeasureType -> {
                    if (measureType.getUuid() != null) {
                        existingMeasureType.setUuid(measureType.getUuid());
                    }
                    if (measureType.getName() != null) {
                        existingMeasureType.setName(measureType.getName());
                    }
                    if (measureType.getImage() != null) {
                        existingMeasureType.setImage(measureType.getImage());
                    }
                    if (measureType.getImageContentType() != null) {
                        existingMeasureType.setImageContentType(measureType.getImageContentType());
                    }
                    if (measureType.getNote() != null) {
                        existingMeasureType.setNote(measureType.getNote());
                    }

                    return existingMeasureType;
                }
            )
            .map(measureTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasureType> findAll(Pageable pageable) {
        log.debug("Request to get all MeasureTypes");
        return measureTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeasureType> findOne(Long id) {
        log.debug("Request to get MeasureType : {}", id);
        return measureTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MeasureType : {}", id);
        measureTypeRepository.deleteById(id);
    }
}
