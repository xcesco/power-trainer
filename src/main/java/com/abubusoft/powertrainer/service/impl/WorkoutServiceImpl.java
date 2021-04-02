package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.repository.WorkoutRepository;
import com.abubusoft.powertrainer.service.WorkoutService;
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

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public Workout save(Workout workout) {
        log.debug("Request to save Workout : {}", workout);
        return workoutRepository.save(workout);
    }

    @Override
    public Optional<Workout> partialUpdate(Workout workout) {
        log.debug("Request to partially update Workout : {}", workout);

        return workoutRepository
            .findById(workout.getId())
            .map(
                existingWorkout -> {
                    if (workout.getUuid() != null) {
                        existingWorkout.setUuid(workout.getUuid());
                    }
                    if (workout.getName() != null) {
                        existingWorkout.setName(workout.getName());
                    }
                    if (workout.getImage() != null) {
                        existingWorkout.setImage(workout.getImage());
                    }
                    if (workout.getImageContentType() != null) {
                        existingWorkout.setImageContentType(workout.getImageContentType());
                    }
                    if (workout.getType() != null) {
                        existingWorkout.setType(workout.getType());
                    }
                    if (workout.getExecutionTime() != null) {
                        existingWorkout.setExecutionTime(workout.getExecutionTime());
                    }
                    if (workout.getPreviewTime() != null) {
                        existingWorkout.setPreviewTime(workout.getPreviewTime());
                    }
                    if (workout.getStatus() != null) {
                        existingWorkout.setStatus(workout.getStatus());
                    }
                    if (workout.getDate() != null) {
                        existingWorkout.setDate(workout.getDate());
                    }
                    if (workout.getNote() != null) {
                        existingWorkout.setNote(workout.getNote());
                    }

                    return existingWorkout;
                }
            )
            .map(workoutRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Workout> findAll(Pageable pageable) {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Workout> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        workoutRepository.deleteById(id);
    }
}
