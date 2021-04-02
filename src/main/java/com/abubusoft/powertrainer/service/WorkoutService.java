package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.Workout;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Workout}.
 */
public interface WorkoutService {
    /**
     * Save a workout.
     *
     * @param workout the entity to save.
     * @return the persisted entity.
     */
    Workout save(Workout workout);

    /**
     * Partially updates a workout.
     *
     * @param workout the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Workout> partialUpdate(Workout workout);

    /**
     * Get all the workouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Workout> findAll(Pageable pageable);

    /**
     * Get the "id" workout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Workout> findOne(Long id);

    /**
     * Delete the "id" workout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
