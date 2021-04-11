package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.repository.ExerciseRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseCriteria;
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
 * Service for executing complex queries for {@link Exercise} entities in the database.
 * The main input is a {@link ExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Exercise} or a {@link Page} of {@link Exercise} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseQueryService extends QueryService<Exercise> {

    private final Logger log = LoggerFactory.getLogger(ExerciseQueryService.class);

    private final ExerciseRepository exerciseRepository;

    public ExerciseQueryService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Return a {@link List} of {@link Exercise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Exercise> findByCriteria(ExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Exercise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Exercise> findByCriteria(ExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Exercise> createSpecification(ExerciseCriteria criteria) {
        Specification<Exercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Exercise_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Exercise_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Exercise_.name));
            }
            if (criteria.getValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getValueType(), Exercise_.valueType));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Exercise_.owner));
            }
            if (criteria.getNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNoteId(), root -> root.join(Exercise_.notes, JoinType.LEFT).get(Note_.id))
                    );
            }
            if (criteria.getMuscleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMuscleId(), root -> root.join(Exercise_.muscles, JoinType.LEFT).get(Muscle_.id))
                    );
            }
            if (criteria.getExerciseToolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExerciseToolId(),
                            root -> root.join(Exercise_.exerciseTools, JoinType.LEFT).get(ExerciseTool_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
