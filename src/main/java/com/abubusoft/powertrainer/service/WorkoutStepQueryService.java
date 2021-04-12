package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutStepCriteria;
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
 * Service for executing complex queries for {@link WorkoutStep} entities in the database.
 * The main input is a {@link WorkoutStepCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkoutStep} or a {@link Page} of {@link WorkoutStep} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkoutStepQueryService extends QueryService<WorkoutStep> {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepQueryService.class);

    private final WorkoutStepRepository workoutStepRepository;

    public WorkoutStepQueryService(WorkoutStepRepository workoutStepRepository) {
        this.workoutStepRepository = workoutStepRepository;
    }

    /**
     * Return a {@link List} of {@link WorkoutStep} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkoutStep> findByCriteria(WorkoutStepCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkoutStep> specification = createSpecification(criteria);
        return workoutStepRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WorkoutStep} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkoutStep> findByCriteria(WorkoutStepCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkoutStep> specification = createSpecification(criteria);
        return workoutStepRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkoutStepCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkoutStep> specification = createSpecification(criteria);
        return workoutStepRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkoutStepCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkoutStep> createSpecification(WorkoutStepCriteria criteria) {
        Specification<WorkoutStep> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkoutStep_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), WorkoutStep_.uuid));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), WorkoutStep_.order));
            }
            if (criteria.getExecutionTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecutionTime(), WorkoutStep_.executionTime));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), WorkoutStep_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), WorkoutStep_.status));
            }
            if (criteria.getExerciseUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseUuid(), WorkoutStep_.exerciseUuid));
            }
            if (criteria.getExerciseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExerciseName(), WorkoutStep_.exerciseName));
            }
            if (criteria.getExerciseValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExerciseValue(), WorkoutStep_.exerciseValue));
            }
            if (criteria.getExerciseValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseValueType(), WorkoutStep_.exerciseValueType));
            }
            if (criteria.getWorkoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWorkoutId(), root -> root.join(WorkoutStep_.workout, JoinType.LEFT).get(Workout_.id))
                    );
            }
        }
        return specification;
    }
}
