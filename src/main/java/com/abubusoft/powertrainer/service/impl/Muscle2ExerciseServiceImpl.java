package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import com.abubusoft.powertrainer.repository.Muscle2ExerciseRepository;
import com.abubusoft.powertrainer.service.Muscle2ExerciseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Muscle2Exercise}.
 */
@Service
@Transactional
public class Muscle2ExerciseServiceImpl implements Muscle2ExerciseService {

    private final Logger log = LoggerFactory.getLogger(Muscle2ExerciseServiceImpl.class);

    private final Muscle2ExerciseRepository muscle2ExerciseRepository;

    public Muscle2ExerciseServiceImpl(Muscle2ExerciseRepository muscle2ExerciseRepository) {
        this.muscle2ExerciseRepository = muscle2ExerciseRepository;
    }

    @Override
    public Muscle2Exercise save(Muscle2Exercise muscle2Exercise) {
        log.debug("Request to save Muscle2Exercise : {}", muscle2Exercise);
        return muscle2ExerciseRepository.save(muscle2Exercise);
    }

    @Override
    public Optional<Muscle2Exercise> partialUpdate(Muscle2Exercise muscle2Exercise) {
        log.debug("Request to partially update Muscle2Exercise : {}", muscle2Exercise);

        return muscle2ExerciseRepository
            .findById(muscle2Exercise.getId())
            .map(
                existingMuscle2Exercise -> {
                    return existingMuscle2Exercise;
                }
            )
            .map(muscle2ExerciseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Muscle2Exercise> findAll(Pageable pageable) {
        log.debug("Request to get all Muscle2Exercises");
        return muscle2ExerciseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Muscle2Exercise> findOne(Long id) {
        log.debug("Request to get Muscle2Exercise : {}", id);
        return muscle2ExerciseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Muscle2Exercise : {}", id);
        muscle2ExerciseRepository.deleteById(id);
    }
}
