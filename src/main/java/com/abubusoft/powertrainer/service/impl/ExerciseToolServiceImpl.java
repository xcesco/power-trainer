package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.repository.ExerciseToolRepository;
import com.abubusoft.powertrainer.service.ExerciseToolService;
import com.abubusoft.powertrainer.service.dto.ExerciseToolDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseToolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseTool}.
 */
@Service
@Transactional
public class ExerciseToolServiceImpl implements ExerciseToolService {

    private final Logger log = LoggerFactory.getLogger(ExerciseToolServiceImpl.class);

    private final ExerciseToolRepository exerciseToolRepository;

    private final ExerciseToolMapper exerciseToolMapper;

    public ExerciseToolServiceImpl(ExerciseToolRepository exerciseToolRepository, ExerciseToolMapper exerciseToolMapper) {
        this.exerciseToolRepository = exerciseToolRepository;
        this.exerciseToolMapper = exerciseToolMapper;
    }

    @Override
    public ExerciseToolDTO save(ExerciseToolDTO exerciseToolDTO) {
        log.debug("Request to save ExerciseTool : {}", exerciseToolDTO);
        ExerciseTool exerciseTool = exerciseToolMapper.toEntity(exerciseToolDTO);
        exerciseTool = exerciseToolRepository.save(exerciseTool);
        return exerciseToolMapper.toDto(exerciseTool);
    }

    @Override
    public Optional<ExerciseToolDTO> partialUpdate(ExerciseToolDTO exerciseToolDTO) {
        log.debug("Request to partially update ExerciseTool : {}", exerciseToolDTO);

        return exerciseToolRepository
            .findById(exerciseToolDTO.getId())
            .map(
                existingExerciseTool -> {
                    exerciseToolMapper.partialUpdate(existingExerciseTool, exerciseToolDTO);
                    return existingExerciseTool;
                }
            )
            .map(exerciseToolRepository::save)
            .map(exerciseToolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseToolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseTools");
        return exerciseToolRepository.findAll(pageable).map(exerciseToolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseToolDTO> findOne(Long id) {
        log.debug("Request to get ExerciseTool : {}", id);
        return exerciseToolRepository.findById(id).map(exerciseToolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseTool : {}", id);
        exerciseToolRepository.deleteById(id);
    }
}
