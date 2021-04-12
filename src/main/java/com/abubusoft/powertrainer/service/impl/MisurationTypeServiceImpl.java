package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.MisurationType;
import com.abubusoft.powertrainer.repository.MisurationTypeRepository;
import com.abubusoft.powertrainer.service.MisurationTypeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MisurationType}.
 */
@Service
@Transactional
public class MisurationTypeServiceImpl implements MisurationTypeService {

    private final Logger log = LoggerFactory.getLogger(MisurationTypeServiceImpl.class);

    private final MisurationTypeRepository misurationTypeRepository;

    public MisurationTypeServiceImpl(MisurationTypeRepository misurationTypeRepository) {
        this.misurationTypeRepository = misurationTypeRepository;
    }

    @Override
    public MisurationType save(MisurationType misurationType) {
        log.debug("Request to save MisurationType : {}", misurationType);
        return misurationTypeRepository.save(misurationType);
    }

    @Override
    public Optional<MisurationType> partialUpdate(MisurationType misurationType) {
        log.debug("Request to partially update MisurationType : {}", misurationType);

        return misurationTypeRepository
            .findById(misurationType.getId())
            .map(
                existingMisurationType -> {
                    if (misurationType.getUuid() != null) {
                        existingMisurationType.setUuid(misurationType.getUuid());
                    }
                    if (misurationType.getName() != null) {
                        existingMisurationType.setName(misurationType.getName());
                    }
                    if (misurationType.getImage() != null) {
                        existingMisurationType.setImage(misurationType.getImage());
                    }
                    if (misurationType.getImageContentType() != null) {
                        existingMisurationType.setImageContentType(misurationType.getImageContentType());
                    }
                    if (misurationType.getDescription() != null) {
                        existingMisurationType.setDescription(misurationType.getDescription());
                    }

                    return existingMisurationType;
                }
            )
            .map(misurationTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MisurationType> findAll(Pageable pageable) {
        log.debug("Request to get all MisurationTypes");
        return misurationTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MisurationType> findOne(Long id) {
        log.debug("Request to get MisurationType : {}", id);
        return misurationTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MisurationType : {}", id);
        misurationTypeRepository.deleteById(id);
    }
}
