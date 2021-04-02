package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.MeasureType;
import com.abubusoft.powertrainer.repository.MeasureTypeRepository;
import com.abubusoft.powertrainer.service.criteria.MeasureTypeCriteria;
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
 * Service for executing complex queries for {@link MeasureType} entities in the database.
 * The main input is a {@link MeasureTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeasureType} or a {@link Page} of {@link MeasureType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeasureTypeQueryService extends QueryService<MeasureType> {

    private final Logger log = LoggerFactory.getLogger(MeasureTypeQueryService.class);

    private final MeasureTypeRepository measureTypeRepository;

    public MeasureTypeQueryService(MeasureTypeRepository measureTypeRepository) {
        this.measureTypeRepository = measureTypeRepository;
    }

    /**
     * Return a {@link List} of {@link MeasureType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeasureType> findByCriteria(MeasureTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MeasureType> specification = createSpecification(criteria);
        return measureTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MeasureType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeasureType> findByCriteria(MeasureTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MeasureType> specification = createSpecification(criteria);
        return measureTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeasureTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MeasureType> specification = createSpecification(criteria);
        return measureTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link MeasureTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MeasureType> createSpecification(MeasureTypeCriteria criteria) {
        Specification<MeasureType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MeasureType_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), MeasureType_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MeasureType_.name));
            }
        }
        return specification;
    }
}
