package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Muscle;
import com.abubusoft.powertrainer.repository.MuscleRepository;
import com.abubusoft.powertrainer.service.MuscleService;
import com.abubusoft.powertrainer.service.dto.MuscleDTO;
import com.abubusoft.powertrainer.service.mapper.MuscleMapper;
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

    private final MuscleMapper muscleMapper;

    public MuscleServiceImpl(MuscleRepository muscleRepository, MuscleMapper muscleMapper) {
        this.muscleRepository = muscleRepository;
        this.muscleMapper = muscleMapper;
    }

    @Override
    public MuscleDTO save(MuscleDTO muscleDTO) {
        log.debug("Request to save Muscle : {}", muscleDTO);
        Muscle muscle = muscleMapper.toEntity(muscleDTO);
        muscle = muscleRepository.save(muscle);
        return muscleMapper.toDto(muscle);
    }

    @Override
    public Optional<MuscleDTO> partialUpdate(MuscleDTO muscleDTO) {
        log.debug("Request to partially update Muscle : {}", muscleDTO);

        return muscleRepository
            .findById(muscleDTO.getId())
            .map(
                existingMuscle -> {
                    muscleMapper.partialUpdate(existingMuscle, muscleDTO);
                    return existingMuscle;
                }
            )
            .map(muscleRepository::save)
            .map(muscleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MuscleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Muscles");
        return muscleRepository.findAll(pageable).map(muscleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MuscleDTO> findOne(Long id) {
        log.debug("Request to get Muscle : {}", id);
        return muscleRepository.findById(id).map(muscleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Muscle : {}", id);
        muscleRepository.deleteById(id);
    }
}
