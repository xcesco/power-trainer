package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import com.abubusoft.powertrainer.repository.WorkoutSheetExerciseRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetExerciseService;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetExerciseDTO;
import com.abubusoft.powertrainer.service.mapper.WorkoutSheetExerciseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutSheetExercise}.
 */
@Service
@Transactional
public class WorkoutSheetExerciseServiceImpl implements WorkoutSheetExerciseService {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetExerciseServiceImpl.class);

    private final WorkoutSheetExerciseRepository workoutSheetExerciseRepository;

    private final WorkoutSheetExerciseMapper workoutSheetExerciseMapper;

    public WorkoutSheetExerciseServiceImpl(
        WorkoutSheetExerciseRepository workoutSheetExerciseRepository,
        WorkoutSheetExerciseMapper workoutSheetExerciseMapper
    ) {
        this.workoutSheetExerciseRepository = workoutSheetExerciseRepository;
        this.workoutSheetExerciseMapper = workoutSheetExerciseMapper;
    }

    @Override
    public WorkoutSheetExerciseDTO save(WorkoutSheetExerciseDTO workoutSheetExerciseDTO) {
        log.debug("Request to save WorkoutSheetExercise : {}", workoutSheetExerciseDTO);
        WorkoutSheetExercise workoutSheetExercise = workoutSheetExerciseMapper.toEntity(workoutSheetExerciseDTO);
        workoutSheetExercise = workoutSheetExerciseRepository.save(workoutSheetExercise);
        return workoutSheetExerciseMapper.toDto(workoutSheetExercise);
    }

    @Override
    public Optional<WorkoutSheetExerciseDTO> partialUpdate(WorkoutSheetExerciseDTO workoutSheetExerciseDTO) {
        log.debug("Request to partially update WorkoutSheetExercise : {}", workoutSheetExerciseDTO);

        return workoutSheetExerciseRepository
            .findById(workoutSheetExerciseDTO.getId())
            .map(
                existingWorkoutSheetExercise -> {
                    workoutSheetExerciseMapper.partialUpdate(existingWorkoutSheetExercise, workoutSheetExerciseDTO);
                    return existingWorkoutSheetExercise;
                }
            )
            .map(workoutSheetExerciseRepository::save)
            .map(workoutSheetExerciseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutSheetExerciseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSheetExercises");
        return workoutSheetExerciseRepository.findAll(pageable).map(workoutSheetExerciseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutSheetExerciseDTO> findOne(Long id) {
        log.debug("Request to get WorkoutSheetExercise : {}", id);
        return workoutSheetExerciseRepository.findById(id).map(workoutSheetExerciseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutSheetExercise : {}", id);
        workoutSheetExerciseRepository.deleteById(id);
    }
}
