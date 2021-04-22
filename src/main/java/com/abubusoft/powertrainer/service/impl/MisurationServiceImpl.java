package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.repository.MisurationRepository;
import com.abubusoft.powertrainer.service.MisurationService;
import com.abubusoft.powertrainer.service.dto.MisurationDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationMapper;
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

    private final MisurationMapper misurationMapper;

    public MisurationServiceImpl(MisurationRepository misurationRepository, MisurationMapper misurationMapper) {
        this.misurationRepository = misurationRepository;
        this.misurationMapper = misurationMapper;
    }

    @Override
    public MisurationDTO save(MisurationDTO misurationDTO) {
        log.debug("Request to save Misuration : {}", misurationDTO);
        Misuration misuration = misurationMapper.toEntity(misurationDTO);
        misuration = misurationRepository.save(misuration);
        return misurationMapper.toDto(misuration);
    }

    @Override
    public Optional<MisurationDTO> partialUpdate(MisurationDTO misurationDTO) {
        log.debug("Request to partially update Misuration : {}", misurationDTO);

        return misurationRepository
            .findById(misurationDTO.getId())
            .map(
                existingMisuration -> {
                    misurationMapper.partialUpdate(existingMisuration, misurationDTO);
                    return existingMisuration;
                }
            )
            .map(misurationRepository::save)
            .map(misurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MisurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Misurations");
        return misurationRepository.findAll(pageable).map(misurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MisurationDTO> findOne(Long id) {
        log.debug("Request to get Misuration : {}", id);
        return misurationRepository.findById(id).map(misurationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Misuration : {}", id);
        misurationRepository.deleteById(id);
    }
}
