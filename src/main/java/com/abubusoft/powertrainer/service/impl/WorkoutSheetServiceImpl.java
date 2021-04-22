package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.WorkoutSheet;
import com.abubusoft.powertrainer.repository.WorkoutSheetRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetService;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetDTO;
import com.abubusoft.powertrainer.service.mapper.WorkoutSheetMapper;
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

    private final WorkoutSheetMapper workoutSheetMapper;

    public WorkoutSheetServiceImpl(WorkoutSheetRepository workoutSheetRepository, WorkoutSheetMapper workoutSheetMapper) {
        this.workoutSheetRepository = workoutSheetRepository;
        this.workoutSheetMapper = workoutSheetMapper;
    }

    @Override
    public WorkoutSheetDTO save(WorkoutSheetDTO workoutSheetDTO) {
        log.debug("Request to save WorkoutSheet : {}", workoutSheetDTO);
        WorkoutSheet workoutSheet = workoutSheetMapper.toEntity(workoutSheetDTO);
        workoutSheet = workoutSheetRepository.save(workoutSheet);
        return workoutSheetMapper.toDto(workoutSheet);
    }

    @Override
    public Optional<WorkoutSheetDTO> partialUpdate(WorkoutSheetDTO workoutSheetDTO) {
        log.debug("Request to partially update WorkoutSheet : {}", workoutSheetDTO);

        return workoutSheetRepository
            .findById(workoutSheetDTO.getId())
            .map(
                existingWorkoutSheet -> {
                    workoutSheetMapper.partialUpdate(existingWorkoutSheet, workoutSheetDTO);
                    return existingWorkoutSheet;
                }
            )
            .map(workoutSheetRepository::save)
            .map(workoutSheetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutSheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSheets");
        return workoutSheetRepository.findAll(pageable).map(workoutSheetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutSheetDTO> findOne(Long id) {
        log.debug("Request to get WorkoutSheet : {}", id);
        return workoutSheetRepository.findById(id).map(workoutSheetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutSheet : {}", id);
        workoutSheetRepository.deleteById(id);
    }
}
