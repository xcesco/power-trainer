package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.repository.ExerciseResourceRepository;
import com.abubusoft.powertrainer.service.ExerciseResourceService;
import com.abubusoft.powertrainer.service.dto.ExerciseResourceDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseResourceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseResource}.
 */
@Service
@Transactional
public class ExerciseResourceServiceImpl implements ExerciseResourceService {

    private final Logger log = LoggerFactory.getLogger(ExerciseResourceServiceImpl.class);

    private final ExerciseResourceRepository exerciseResourceRepository;

    private final ExerciseResourceMapper exerciseResourceMapper;

    public ExerciseResourceServiceImpl(
        ExerciseResourceRepository exerciseResourceRepository,
        ExerciseResourceMapper exerciseResourceMapper
    ) {
        this.exerciseResourceRepository = exerciseResourceRepository;
        this.exerciseResourceMapper = exerciseResourceMapper;
    }

    @Override
    public ExerciseResourceDTO save(ExerciseResourceDTO exerciseResourceDTO) {
        log.debug("Request to save ExerciseResource : {}", exerciseResourceDTO);
        ExerciseResource exerciseResource = exerciseResourceMapper.toEntity(exerciseResourceDTO);
        exerciseResource = exerciseResourceRepository.save(exerciseResource);
        return exerciseResourceMapper.toDto(exerciseResource);
    }

    @Override
    public Optional<ExerciseResourceDTO> partialUpdate(ExerciseResourceDTO exerciseResourceDTO) {
        log.debug("Request to partially update ExerciseResource : {}", exerciseResourceDTO);

        return exerciseResourceRepository
            .findById(exerciseResourceDTO.getId())
            .map(
                existingExerciseResource -> {
                    exerciseResourceMapper.partialUpdate(existingExerciseResource, exerciseResourceDTO);
                    return existingExerciseResource;
                }
            )
            .map(exerciseResourceRepository::save)
            .map(exerciseResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseResources");
        return exerciseResourceRepository.findAll(pageable).map(exerciseResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseResourceDTO> findOne(Long id) {
        log.debug("Request to get ExerciseResource : {}", id);
        return exerciseResourceRepository.findById(id).map(exerciseResourceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseResource : {}", id);
        exerciseResourceRepository.deleteById(id);
    }
}
