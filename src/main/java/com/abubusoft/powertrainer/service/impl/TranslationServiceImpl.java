package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.TranslationRepository;
import com.abubusoft.powertrainer.service.TranslationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Translation}.
 */
@Service
@Transactional
public class TranslationServiceImpl implements TranslationService {

    private final Logger log = LoggerFactory.getLogger(TranslationServiceImpl.class);

    private final TranslationRepository translationRepository;

    public TranslationServiceImpl(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Override
    public Translation save(Translation translation) {
        log.debug("Request to save Translation : {}", translation);
        return translationRepository.save(translation);
    }

    @Override
    public Optional<Translation> partialUpdate(Translation translation) {
        log.debug("Request to partially update Translation : {}", translation);

        return translationRepository
            .findById(translation.getId())
            .map(
                existingTranslation -> {
                    if (translation.getEntityType() != null) {
                        existingTranslation.setEntityType(translation.getEntityType());
                    }
                    if (translation.getEntityUuid() != null) {
                        existingTranslation.setEntityUuid(translation.getEntityUuid());
                    }
                    if (translation.getValue() != null) {
                        existingTranslation.setValue(translation.getValue());
                    }

                    return existingTranslation;
                }
            )
            .map(translationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Translation> findAll(Pageable pageable) {
        log.debug("Request to get all Translations");
        return translationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Translation> findOne(Long id) {
        log.debug("Request to get Translation : {}", id);
        return translationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Translation : {}", id);
        translationRepository.deleteById(id);
    }
}
