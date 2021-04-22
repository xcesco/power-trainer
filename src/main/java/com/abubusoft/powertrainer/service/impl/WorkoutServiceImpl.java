package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.repository.WorkoutRepository;
import com.abubusoft.powertrainer.service.WorkoutService;
import com.abubusoft.powertrainer.service.dto.WorkoutDTO;
import com.abubusoft.powertrainer.service.mapper.WorkoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Workout}.
 */
@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

    private final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository workoutRepository;

    private final WorkoutMapper workoutMapper;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }

    @Override
    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        log.debug("Request to save Workout : {}", workoutDTO);
        Workout workout = workoutMapper.toEntity(workoutDTO);
        workout = workoutRepository.save(workout);
        return workoutMapper.toDto(workout);
    }

    @Override
    public Optional<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO) {
        log.debug("Request to partially update Workout : {}", workoutDTO);

        return workoutRepository
            .findById(workoutDTO.getId())
            .map(
                existingWorkout -> {
                    workoutMapper.partialUpdate(existingWorkout, workoutDTO);
                    return existingWorkout;
                }
            )
            .map(workoutRepository::save)
            .map(workoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll(pageable).map(workoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutDTO> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id).map(workoutMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        workoutRepository.deleteById(id);
    }
}
