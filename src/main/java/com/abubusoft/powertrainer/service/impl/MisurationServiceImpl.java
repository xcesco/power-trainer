package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.repository.MisurationRepository;
import com.abubusoft.powertrainer.service.MisurationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Misuration}.
 */
@Service
@Transactional
public class MisurationServiceImpl implements MisurationService {

    private final Logger log = LoggerFactory.getLogger(MisurationServiceImpl.class);

    private final MisurationRepository misurationRepository;

    public MisurationServiceImpl(MisurationRepository misurationRepository) {
        this.misurationRepository = misurationRepository;
    }

    @Override
    public Misuration save(Misuration misuration) {
        log.debug("Request to save Misuration : {}", misuration);
        return misurationRepository.save(misuration);
    }

    @Override
    public Optional<Misuration> partialUpdate(Misuration misuration) {
        log.debug("Request to partially update Misuration : {}", misuration);

        return misurationRepository
            .findById(misuration.getId())
            .map(
                existingMisuration -> {
                    if (misuration.getUuid() != null) {
                        existingMisuration.setUuid(misuration.getUuid());
                    }
                    if (misuration.getDate() != null) {
                        existingMisuration.setDate(misuration.getDate());
                    }
                    if (misuration.getValue() != null) {
                        existingMisuration.setValue(misuration.getValue());
                    }
                    if (misuration.getImage() != null) {
                        existingMisuration.setImage(misuration.getImage());
                    }
                    if (misuration.getImageContentType() != null) {
                        existingMisuration.setImageContentType(misuration.getImageContentType());
                    }
                    if (misuration.getNote() != null) {
                        existingMisuration.setNote(misuration.getNote());
                    }

                    return existingMisuration;
                }
            )
            .map(misurationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Misuration> findAll(Pageable pageable) {
        log.debug("Request to get all Misurations");
        return misurationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Misuration> findOne(Long id) {
        log.debug("Request to get Misuration : {}", id);
        return misurationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Misuration : {}", id);
        misurationRepository.deleteById(id);
    }
}
