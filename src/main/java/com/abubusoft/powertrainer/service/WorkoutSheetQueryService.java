package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.WorkoutSheet;
import com.abubusoft.powertrainer.repository.WorkoutSheetRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetCriteria;
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
 * Service for executing complex queries for {@link WorkoutSheet} entities in the database.
 * The main input is a {@link WorkoutSheetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkoutSheet} or a {@link Page} of {@link WorkoutSheet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkoutSheetQueryService extends QueryService<WorkoutSheet> {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetQueryService.class);

    private final WorkoutSheetRepository workoutSheetRepository;

    public WorkoutSheetQueryService(WorkoutSheetRepository workoutSheetRepository) {
        this.workoutSheetRepository = workoutSheetRepository;
    }

    /**
     * Return a {@link List} of {@link WorkoutSheet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkoutSheet> findByCriteria(WorkoutSheetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkoutSheet> specification = createSpecification(criteria);
        return workoutSheetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WorkoutSheet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkoutSheet> findByCriteria(WorkoutSheetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkoutSheet> specification = createSpecification(criteria);
        return workoutSheetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkoutSheetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkoutSheet> specification = createSpecification(criteria);
        return workoutSheetRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkoutSheetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkoutSheet> createSpecification(WorkoutSheetCriteria criteria) {
        Specification<WorkoutSheet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkoutSheet_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), WorkoutSheet_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WorkoutSheet_.name));
            }
            if (criteria.getPrepareTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrepareTime(), WorkoutSheet_.prepareTime));
            }
            if (criteria.getCoolDownTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoolDownTime(), WorkoutSheet_.coolDownTime));
            }
            if (criteria.getCycles() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCycles(), WorkoutSheet_.cycles));
            }
            if (criteria.getCycleRestTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCycleRestTime(), WorkoutSheet_.cycleRestTime));
            }
            if (criteria.getSet() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSet(), WorkoutSheet_.set));
            }
            if (criteria.getSetRestTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSetRestTime(), WorkoutSheet_.setRestTime));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), WorkoutSheet_.type));
            }
        }
        return specification;
    }
}
