package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.repository.ExerciseRepository;
import com.abubusoft.powertrainer.service.ExerciseService;
import com.abubusoft.powertrainer.service.dto.ExerciseDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exercise}.
 */
@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

    private final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public ExerciseDTO save(ExerciseDTO exerciseDTO) {
        log.debug("Request to save Exercise : {}", exerciseDTO);
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        exercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDto(exercise);
    }

    @Override
    public Optional<ExerciseDTO> partialUpdate(ExerciseDTO exerciseDTO) {
        log.debug("Request to partially update Exercise : {}", exerciseDTO);

        return exerciseRepository
            .findById(exerciseDTO.getId())
            .map(
                existingExercise -> {
                    exerciseMapper.partialUpdate(existingExercise, exerciseDTO);
                    return existingExercise;
                }
            )
            .map(exerciseRepository::save)
            .map(exerciseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exercises");
        return exerciseRepository.findAll(pageable).map(exerciseMapper::toDto);
    }

    public Page<ExerciseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return exerciseRepository.findAllWithEagerRelationships(pageable).map(exerciseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseDTO> findOne(Long id) {
        log.debug("Request to get Exercise : {}", id);
        return exerciseRepository.findOneWithEagerRelationships(id).map(exerciseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
    }
}
