package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.repository.ExerciseResourceRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseResourceCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseResourceDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseResourceMapper;
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
 * Service for executing complex queries for {@link ExerciseResource} entities in the database.
 * The main input is a {@link ExerciseResourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseResourceDTO} or a {@link Page} of {@link ExerciseResourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseResourceQueryService extends QueryService<ExerciseResource> {

    private final Logger log = LoggerFactory.getLogger(ExerciseResourceQueryService.class);

    private final ExerciseResourceRepository exerciseResourceRepository;

    private final ExerciseResourceMapper exerciseResourceMapper;

    public ExerciseResourceQueryService(
        ExerciseResourceRepository exerciseResourceRepository,
        ExerciseResourceMapper exerciseResourceMapper
    ) {
        this.exerciseResourceRepository = exerciseResourceRepository;
        this.exerciseResourceMapper = exerciseResourceMapper;
    }

    /**
     * Return a {@link List} of {@link ExerciseResourceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseResourceDTO> findByCriteria(ExerciseResourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExerciseResource> specification = createSpecification(criteria);
        return exerciseResourceMapper.toDto(exerciseResourceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExerciseResourceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseResourceDTO> findByCriteria(ExerciseResourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExerciseResource> specification = createSpecification(criteria);
        return exerciseResourceRepository.findAll(specification, page).map(exerciseResourceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseResourceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExerciseResource> specification = createSpecification(criteria);
        return exerciseResourceRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseResourceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExerciseResource> createSpecification(ExerciseResourceCriteria criteria) {
        Specification<ExerciseResource> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExerciseResource_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), ExerciseResource_.uuid));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), ExerciseResource_.order));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ExerciseResource_.type));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), ExerciseResource_.url));
            }
            if (criteria.getExerciseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExerciseId(),
                            root -> root.join(ExerciseResource_.exercise, JoinType.LEFT).get(Exercise_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
