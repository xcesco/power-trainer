package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import com.abubusoft.powertrainer.repository.WorkoutSheetExerciseRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetExerciseCriteria;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetExerciseDTO;
import com.abubusoft.powertrainer.service.mapper.WorkoutSheetExerciseMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WorkoutSheetExercise} entities in the database.
 * The main input is a {@link WorkoutSheetExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkoutSheetExerciseDTO} or a {@link Page} of {@link WorkoutSheetExerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkoutSheetExerciseQueryService extends QueryService<WorkoutSheetExercise> {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetExerciseQueryService.class);

    private final WorkoutSheetExerciseRepository workoutSheetExerciseRepository;

    private final WorkoutSheetExerciseMapper workoutSheetExerciseMapper;

    public WorkoutSheetExerciseQueryService(
        WorkoutSheetExerciseRepository workoutSheetExerciseRepository,
        WorkoutSheetExerciseMapper workoutSheetExerciseMapper
    ) {
        this.workoutSheetExerciseRepository = workoutSheetExerciseRepository;
        this.workoutSheetExerciseMapper = workoutSheetExerciseMapper;
    }

    /**
     * Return a {@link List} of {@link WorkoutSheetExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkoutSheetExerciseDTO> findByCriteria(WorkoutSheetExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkoutSheetExercise> specification = createSpecification(criteria);
        return workoutSheetExerciseMapper.toDto(workoutSheetExerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkoutSheetExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkoutSheetExerciseDTO> findByCriteria(WorkoutSheetExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkoutSheetExercise> specification = createSpecification(criteria);
        return workoutSheetExerciseRepository.findAll(specification, page).map(workoutSheetExerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkoutSheetExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkoutSheetExercise> specification = createSpecification(criteria);
        return workoutSheetExerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkoutSheetExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkoutSheetExercise> createSpecification(WorkoutSheetExerciseCriteria criteria) {
        Specification<WorkoutSheetExercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkoutSheetExercise_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), WorkoutSheetExercise_.uuid));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), WorkoutSheetExercise_.order));
            }
            if (criteria.getRepetitions() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRepetitions(), WorkoutSheetExercise_.repetitions));
            }
            if (criteria.getExerciseUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseUuid(), WorkoutSheetExercise_.exerciseUuid));
            }
            if (criteria.getExerciseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExerciseName(), WorkoutSheetExercise_.exerciseName));
            }
            if (criteria.getExerciseValue() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getExerciseValue(), WorkoutSheetExercise_.exerciseValue));
            }
            if (criteria.getExerciseValueType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getExerciseValueType(), WorkoutSheetExercise_.exerciseValueType));
            }
            if (criteria.getWorkoutSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkoutSheetId(),
                            root -> root.join(WorkoutSheetExercise_.workoutSheet, JoinType.LEFT).get(WorkoutSheet_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
