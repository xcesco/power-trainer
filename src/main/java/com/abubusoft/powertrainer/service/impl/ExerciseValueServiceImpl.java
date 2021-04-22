package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.ExerciseValueService;
import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseValue}.
 */
@Service
@Transactional
public class ExerciseValueServiceImpl implements ExerciseValueService {

    private final Logger log = LoggerFactory.getLogger(ExerciseValueServiceImpl.class);

    private final ExerciseValueRepository exerciseValueRepository;

    private final ExerciseValueMapper exerciseValueMapper;

    public ExerciseValueServiceImpl(ExerciseValueRepository exerciseValueRepository, ExerciseValueMapper exerciseValueMapper) {
        this.exerciseValueRepository = exerciseValueRepository;
        this.exerciseValueMapper = exerciseValueMapper;
    }

    @Override
    public ExerciseValueDTO save(ExerciseValueDTO exerciseValueDTO) {
        log.debug("Request to save ExerciseValue : {}", exerciseValueDTO);
        ExerciseValue exerciseValue = exerciseValueMapper.toEntity(exerciseValueDTO);
        exerciseValue = exerciseValueRepository.save(exerciseValue);
        return exerciseValueMapper.toDto(exerciseValue);
    }

    @Override
    public Optional<ExerciseValueDTO> partialUpdate(ExerciseValueDTO exerciseValueDTO) {
        log.debug("Request to partially update ExerciseValue : {}", exerciseValueDTO);

        return exerciseValueRepository
            .findById(exerciseValueDTO.getId())
            .map(
                existingExerciseValue -> {
                    exerciseValueMapper.partialUpdate(existingExerciseValue, exerciseValueDTO);
                    return existingExerciseValue;
                }
            )
            .map(exerciseValueRepository::save)
            .map(exerciseValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseValues");
        return exerciseValueRepository.findAll(pageable).map(exerciseValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseValueDTO> findOne(Long id) {
        log.debug("Request to get ExerciseValue : {}", id);
        return exerciseValueRepository.findById(id).map(exerciseValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseValue : {}", id);
        exerciseValueRepository.deleteById(id);
    }
}
