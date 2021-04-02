package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Muscle;
import com.abubusoft.powertrainer.repository.MuscleRepository;
import com.abubusoft.powertrainer.service.MuscleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Muscle}.
 */
@Service
@Transactional
public class MuscleServiceImpl implements MuscleService {

    private final Logger log = LoggerFactory.getLogger(MuscleServiceImpl.class);

    private final MuscleRepository muscleRepository;

    public MuscleServiceImpl(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    @Override
    public Muscle save(Muscle muscle) {
        log.debug("Request to save Muscle : {}", muscle);
        return muscleRepository.save(muscle);
    }

    @Override
    public Optional<Muscle> partialUpdate(Muscle muscle) {
        log.debug("Request to partially update Muscle : {}", muscle);

        return muscleRepository
            .findById(muscle.getId())
            .map(
                existingMuscle -> {
                    if (muscle.getUuid() != null) {
                        existingMuscle.setUuid(muscle.getUuid());
                    }
                    if (muscle.getName() != null) {
                        existingMuscle.setName(muscle.getName());
                    }
                    if (muscle.getImage() != null) {
                        existingMuscle.setImage(muscle.getImage());
                    }
                    if (muscle.getImageContentType() != null) {
                        existingMuscle.setImageContentType(muscle.getImageContentType());
                    }
                    if (muscle.getNote() != null) {
                        existingMuscle.setNote(muscle.getNote());
                    }

                    return existingMuscle;
                }
            )
            .map(muscleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Muscle> findAll(Pageable pageable) {
        log.debug("Request to get all Muscles");
        return muscleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Muscle> findOne(Long id) {
        log.debug("Request to get Muscle : {}", id);
        return muscleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Muscle : {}", id);
        muscleRepository.deleteById(id);
    }
}
