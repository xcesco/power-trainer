package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.WorkoutSheet;
import com.abubusoft.powertrainer.repository.WorkoutSheetRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkoutSheet}.
 */
@Service
@Transactional
public class WorkoutSheetServiceImpl implements WorkoutSheetService {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetServiceImpl.class);

    private final WorkoutSheetRepository workoutSheetRepository;

    public WorkoutSheetServiceImpl(WorkoutSheetRepository workoutSheetRepository) {
        this.workoutSheetRepository = workoutSheetRepository;
    }

    @Override
    public WorkoutSheet save(WorkoutSheet workoutSheet) {
        log.debug("Request to save WorkoutSheet : {}", workoutSheet);
        return workoutSheetRepository.save(workoutSheet);
    }

    @Override
    public Optional<WorkoutSheet> partialUpdate(WorkoutSheet workoutSheet) {
        log.debug("Request to partially update WorkoutSheet : {}", workoutSheet);

        return workoutSheetRepository
            .findById(workoutSheet.getId())
            .map(
                existingWorkoutSheet -> {
                    if (workoutSheet.getUuid() != null) {
                        existingWorkoutSheet.setUuid(workoutSheet.getUuid());
                    }
                    if (workoutSheet.getName() != null) {
                        existingWorkoutSheet.setName(workoutSheet.getName());
                    }
                    if (workoutSheet.getImage() != null) {
                        existingWorkoutSheet.setImage(workoutSheet.getImage());
                    }
                    if (workoutSheet.getImageContentType() != null) {
                        existingWorkoutSheet.setImageContentType(workoutSheet.getImageContentType());
                    }
                    if (workoutSheet.getDescription() != null) {
                        existingWorkoutSheet.setDescription(workoutSheet.getDescription());
                    }
                    if (workoutSheet.getOwner() != null) {
                        existingWorkoutSheet.setOwner(workoutSheet.getOwner());
                    }
                    if (workoutSheet.getPrepareTime() != null) {
                        existingWorkoutSheet.setPrepareTime(workoutSheet.getPrepareTime());
                    }
                    if (workoutSheet.getCoolDownTime() != null) {
                        existingWorkoutSheet.setCoolDownTime(workoutSheet.getCoolDownTime());
                    }
                    if (workoutSheet.getCycles() != null) {
                        existingWorkoutSheet.setCycles(workoutSheet.getCycles());
                    }
                    if (workoutSheet.getCycleRestTime() != null) {
                        existingWorkoutSheet.setCycleRestTime(workoutSheet.getCycleRestTime());
                    }
                    if (workoutSheet.getSet() != null) {
                        existingWorkoutSheet.setSet(workoutSheet.getSet());
                    }
                    if (workoutSheet.getSetRestTime() != null) {
                        existingWorkoutSheet.setSetRestTime(workoutSheet.getSetRestTime());
                    }
                    if (workoutSheet.getType() != null) {
                        existingWorkoutSheet.setType(workoutSheet.getType());
                    }

                    return existingWorkoutSheet;
                }
            )
            .map(workoutSheetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutSheet> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSheets");
        return workoutSheetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutSheet> findOne(Long id) {
        log.debug("Request to get WorkoutSheet : {}", id);
        return workoutSheetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutSheet : {}", id);
        workoutSheetRepository.deleteById(id);
    }
}
