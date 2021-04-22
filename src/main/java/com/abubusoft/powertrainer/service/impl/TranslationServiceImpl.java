package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.TranslationRepository;
import com.abubusoft.powertrainer.service.TranslationService;
import com.abubusoft.powertrainer.service.dto.TranslationDTO;
import com.abubusoft.powertrainer.service.mapper.TranslationMapper;
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

    private final TranslationMapper translationMapper;

    public TranslationServiceImpl(TranslationRepository translationRepository, TranslationMapper translationMapper) {
        this.translationRepository = translationRepository;
        this.translationMapper = translationMapper;
    }

    @Override
    public TranslationDTO save(TranslationDTO translationDTO) {
        log.debug("Request to save Translation : {}", translationDTO);
        Translation translation = translationMapper.toEntity(translationDTO);
        translation = translationRepository.save(translation);
        return translationMapper.toDto(translation);
    }

    @Override
    public Optional<TranslationDTO> partialUpdate(TranslationDTO translationDTO) {
        log.debug("Request to partially update Translation : {}", translationDTO);

        return translationRepository
            .findById(translationDTO.getId())
            .map(
                existingTranslation -> {
                    translationMapper.partialUpdate(existingTranslation, translationDTO);
                    return existingTranslation;
                }
            )
            .map(translationRepository::save)
            .map(translationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Translations");
        return translationRepository.findAll(pageable).map(translationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TranslationDTO> findOne(Long id) {
        log.debug("Request to get Translation : {}", id);
        return translationRepository.findById(id).map(translationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Translation : {}", id);
        translationRepository.deleteById(id);
    }
}
