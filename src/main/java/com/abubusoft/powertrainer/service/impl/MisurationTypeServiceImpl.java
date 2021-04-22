package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.MisurationType;
import com.abubusoft.powertrainer.repository.MisurationTypeRepository;
import com.abubusoft.powertrainer.service.MisurationTypeService;
import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationTypeMapper;
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

    private final MisurationTypeMapper misurationTypeMapper;

    public MisurationTypeServiceImpl(MisurationTypeRepository misurationTypeRepository, MisurationTypeMapper misurationTypeMapper) {
        this.misurationTypeRepository = misurationTypeRepository;
        this.misurationTypeMapper = misurationTypeMapper;
    }

    @Override
    public MisurationTypeDTO save(MisurationTypeDTO misurationTypeDTO) {
        log.debug("Request to save MisurationType : {}", misurationTypeDTO);
        MisurationType misurationType = misurationTypeMapper.toEntity(misurationTypeDTO);
        misurationType = misurationTypeRepository.save(misurationType);
        return misurationTypeMapper.toDto(misurationType);
    }

    @Override
    public Optional<MisurationTypeDTO> partialUpdate(MisurationTypeDTO misurationTypeDTO) {
        log.debug("Request to partially update MisurationType : {}", misurationTypeDTO);

        return misurationTypeRepository
            .findById(misurationTypeDTO.getId())
            .map(
                existingMisurationType -> {
                    misurationTypeMapper.partialUpdate(existingMisurationType, misurationTypeDTO);
                    return existingMisurationType;
                }
            )
            .map(misurationTypeRepository::save)
            .map(misurationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MisurationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MisurationTypes");
        return misurationTypeRepository.findAll(pageable).map(misurationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MisurationTypeDTO> findOne(Long id) {
        log.debug("Request to get MisurationType : {}", id);
        return misurationTypeRepository.findById(id).map(misurationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MisurationType : {}", id);
        misurationTypeRepository.deleteById(id);
    }
}
