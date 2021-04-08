package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.repository.ExerciseToolRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseToolCriteria;
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
 * Service for executing complex queries for {@link ExerciseTool} entities in the database.
 * The main input is a {@link ExerciseToolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseTool} or a {@link Page} of {@link ExerciseTool} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseToolQueryService extends QueryService<ExerciseTool> {

    private final Logger log = LoggerFactory.getLogger(ExerciseToolQueryService.class);

    private final ExerciseToolRepository exerciseToolRepository;

    public ExerciseToolQueryService(ExerciseToolRepository exerciseToolRepository) {
        this.exerciseToolRepository = exerciseToolRepository;
    }

    /**
     * Return a {@link List} of {@link ExerciseTool} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseTool> findByCriteria(ExerciseToolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExerciseTool> specification = createSpecification(criteria);
        return exerciseToolRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExerciseTool} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseTool> findByCriteria(ExerciseToolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExerciseTool> specification = createSpecification(criteria);
        return exerciseToolRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseToolCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExerciseTool> specification = createSpecification(criteria);
        return exerciseToolRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseToolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExerciseTool> createSpecification(ExerciseToolCriteria criteria) {
        Specification<ExerciseTool> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExerciseTool_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), ExerciseTool_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ExerciseTool_.name));
            }
            if (criteria.getExerciseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExerciseId(),
                            root -> root.join(ExerciseTool_.exercise, JoinType.LEFT).get(Exercise_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
