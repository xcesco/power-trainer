package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Muscle;
import com.abubusoft.powertrainer.repository.MuscleRepository;
import com.abubusoft.powertrainer.service.criteria.MuscleCriteria;
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
 * Service for executing complex queries for {@link Muscle} entities in the database.
 * The main input is a {@link MuscleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Muscle} or a {@link Page} of {@link Muscle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MuscleQueryService extends QueryService<Muscle> {

    private final Logger log = LoggerFactory.getLogger(MuscleQueryService.class);

    private final MuscleRepository muscleRepository;

    public MuscleQueryService(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    /**
     * Return a {@link List} of {@link Muscle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Muscle> findByCriteria(MuscleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Muscle> specification = createSpecification(criteria);
        return muscleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Muscle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Muscle> findByCriteria(MuscleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Muscle> specification = createSpecification(criteria);
        return muscleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MuscleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Muscle> specification = createSpecification(criteria);
        return muscleRepository.count(specification);
    }

    /**
     * Function to convert {@link MuscleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Muscle> createSpecification(MuscleCriteria criteria) {
        Specification<Muscle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Muscle_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Muscle_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Muscle_.name));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Muscle_.note));
            }
        }
        return specification;
    }
}
