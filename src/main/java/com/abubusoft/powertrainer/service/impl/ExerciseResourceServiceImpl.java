package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.repository.ExerciseResourceRepository;
import com.abubusoft.powertrainer.service.ExerciseResourceService;
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

    public ExerciseResourceServiceImpl(ExerciseResourceRepository exerciseResourceRepository) {
        this.exerciseResourceRepository = exerciseResourceRepository;
    }

    @Override
    public ExerciseResource save(ExerciseResource exerciseResource) {
        log.debug("Request to save ExerciseResource : {}", exerciseResource);
        return exerciseResourceRepository.save(exerciseResource);
    }

    @Override
    public Optional<ExerciseResource> partialUpdate(ExerciseResource exerciseResource) {
        log.debug("Request to partially update ExerciseResource : {}", exerciseResource);

        return exerciseResourceRepository
            .findById(exerciseResource.getId())
            .map(
                existingExerciseResource -> {
                    if (exerciseResource.getUuid() != null) {
                        existingExerciseResource.setUuid(exerciseResource.getUuid());
                    }
                    if (exerciseResource.getOrder() != null) {
                        existingExerciseResource.setOrder(exerciseResource.getOrder());
                    }
                    if (exerciseResource.getType() != null) {
                        existingExerciseResource.setType(exerciseResource.getType());
                    }
                    if (exerciseResource.getUrl() != null) {
                        existingExerciseResource.setUrl(exerciseResource.getUrl());
                    }
                    if (exerciseResource.getImage() != null) {
                        existingExerciseResource.setImage(exerciseResource.getImage());
                    }
                    if (exerciseResource.getImageContentType() != null) {
                        existingExerciseResource.setImageContentType(exerciseResource.getImageContentType());
                    }
                    if (exerciseResource.getDescription() != null) {
                        existingExerciseResource.setDescription(exerciseResource.getDescription());
                    }

                    return existingExerciseResource;
                }
            )
            .map(exerciseResourceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseResource> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseResources");
        return exerciseResourceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseResource> findOne(Long id) {
        log.debug("Request to get ExerciseResource : {}", id);
        return exerciseResourceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseResource : {}", id);
        exerciseResourceRepository.deleteById(id);
    }
}
