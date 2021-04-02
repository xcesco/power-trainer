package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseValueCriteria;
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
 * Service for executing complex queries for {@link ExerciseValue} entities in the database.
 * The main input is a {@link ExerciseValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseValue} or a {@link Page} of {@link ExerciseValue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseValueQueryService extends QueryService<ExerciseValue> {

    private final Logger log = LoggerFactory.getLogger(ExerciseValueQueryService.class);

    private final ExerciseValueRepository exerciseValueRepository;

    public ExerciseValueQueryService(ExerciseValueRepository exerciseValueRepository) {
        this.exerciseValueRepository = exerciseValueRepository;
    }

    /**
     * Return a {@link List} of {@link ExerciseValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseValue> findByCriteria(ExerciseValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExerciseValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseValue> findByCriteria(ExerciseValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExerciseValue> createSpecification(ExerciseValueCriteria criteria) {
        Specification<ExerciseValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExerciseValue_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), ExerciseValue_.uuid));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), ExerciseValue_.value));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ExerciseValue_.date));
            }
        }
        return specification;
    }
}
