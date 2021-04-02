package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import com.abubusoft.powertrainer.repository.Muscle2ExerciseRepository;
import com.abubusoft.powertrainer.service.criteria.Muscle2ExerciseCriteria;
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
 * Service for executing complex queries for {@link Muscle2Exercise} entities in the database.
 * The main input is a {@link Muscle2ExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Muscle2Exercise} or a {@link Page} of {@link Muscle2Exercise} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Muscle2ExerciseQueryService extends QueryService<Muscle2Exercise> {

    private final Logger log = LoggerFactory.getLogger(Muscle2ExerciseQueryService.class);

    private final Muscle2ExerciseRepository muscle2ExerciseRepository;

    public Muscle2ExerciseQueryService(Muscle2ExerciseRepository muscle2ExerciseRepository) {
        this.muscle2ExerciseRepository = muscle2ExerciseRepository;
    }

    /**
     * Return a {@link List} of {@link Muscle2Exercise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Muscle2Exercise> findByCriteria(Muscle2ExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Muscle2Exercise> specification = createSpecification(criteria);
        return muscle2ExerciseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Muscle2Exercise} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Muscle2Exercise> findByCriteria(Muscle2ExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Muscle2Exercise> specification = createSpecification(criteria);
        return muscle2ExerciseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Muscle2ExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Muscle2Exercise> specification = createSpecification(criteria);
        return muscle2ExerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link Muscle2ExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Muscle2Exercise> createSpecification(Muscle2ExerciseCriteria criteria) {
        Specification<Muscle2Exercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Muscle2Exercise_.id));
            }
        }
        return specification;
    }
}
