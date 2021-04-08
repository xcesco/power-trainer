package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.repository.WorkoutRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutCriteria;
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
 * Service for executing complex queries for {@link Workout} entities in the database.
 * The main input is a {@link WorkoutCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Workout} or a {@link Page} of {@link Workout} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkoutQueryService extends QueryService<Workout> {

    private final Logger log = LoggerFactory.getLogger(WorkoutQueryService.class);

    private final WorkoutRepository workoutRepository;

    public WorkoutQueryService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    /**
     * Return a {@link List} of {@link Workout} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Workout> findByCriteria(WorkoutCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Workout> specification = createSpecification(criteria);
        return workoutRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Workout} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Workout> findByCriteria(WorkoutCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Workout> specification = createSpecification(criteria);
        return workoutRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkoutCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Workout> specification = createSpecification(criteria);
        return workoutRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkoutCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Workout> createSpecification(WorkoutCriteria criteria) {
        Specification<Workout> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Workout_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Workout_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Workout_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Workout_.type));
            }
            if (criteria.getExecutionTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecutionTime(), Workout_.executionTime));
            }
            if (criteria.getPreviewTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPreviewTime(), Workout_.previewTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Workout_.status));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Workout_.date));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Workout_.note));
            }
            if (criteria.getWorkoutStepId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkoutStepId(),
                            root -> root.join(Workout_.workoutSteps, JoinType.LEFT).get(WorkoutStep_.id)
                        )
                    );
            }
            if (criteria.getCalendarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCalendarId(), root -> root.join(Workout_.calendar, JoinType.LEFT).get(Calendar_.id))
                    );
            }
        }
        return specification;
    }
}
