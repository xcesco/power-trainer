package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.MeasureValue;
import com.abubusoft.powertrainer.repository.MeasureValueRepository;
import com.abubusoft.powertrainer.service.criteria.MeasureValueCriteria;
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
 * Service for executing complex queries for {@link MeasureValue} entities in the database.
 * The main input is a {@link MeasureValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeasureValue} or a {@link Page} of {@link MeasureValue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeasureValueQueryService extends QueryService<MeasureValue> {

    private final Logger log = LoggerFactory.getLogger(MeasureValueQueryService.class);

    private final MeasureValueRepository measureValueRepository;

    public MeasureValueQueryService(MeasureValueRepository measureValueRepository) {
        this.measureValueRepository = measureValueRepository;
    }

    /**
     * Return a {@link List} of {@link MeasureValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeasureValue> findByCriteria(MeasureValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MeasureValue> specification = createSpecification(criteria);
        return measureValueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MeasureValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeasureValue> findByCriteria(MeasureValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MeasureValue> specification = createSpecification(criteria);
        return measureValueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeasureValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MeasureValue> specification = createSpecification(criteria);
        return measureValueRepository.count(specification);
    }

    /**
     * Function to convert {@link MeasureValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MeasureValue> createSpecification(MeasureValueCriteria criteria) {
        Specification<MeasureValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MeasureValue_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), MeasureValue_.uuid));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), MeasureValue_.date));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), MeasureValue_.value));
            }
        }
        return specification;
    }
}
