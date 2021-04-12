package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Language;
import com.abubusoft.powertrainer.repository.LanguageRepository;
import com.abubusoft.powertrainer.service.LanguageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Language}.
 */
@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageServiceImpl.class);

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language save(Language language) {
        log.debug("Request to save Language : {}", language);
        return languageRepository.save(language);
    }

    @Override
    public Optional<Language> partialUpdate(Language language) {
        log.debug("Request to partially update Language : {}", language);

        return languageRepository
            .findById(language.getId())
            .map(
                existingLanguage -> {
                    if (language.getCode() != null) {
                        existingLanguage.setCode(language.getCode());
                    }
                    if (language.getName() != null) {
                        existingLanguage.setName(language.getName());
                    }

                    return existingLanguage;
                }
            )
            .map(languageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Language> findAll(Pageable pageable) {
        log.debug("Request to get all Languages");
        return languageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Language> findOne(Long id) {
        log.debug("Request to get Language : {}", id);
        return languageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Language : {}", id);
        languageRepository.deleteById(id);
    }
}
